package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.constants.PzlTypes
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.FunDeclaration
import puzzle.core.parser.ast.node.NamedType
import puzzle.core.parser.ast.node.TypeReference
import puzzle.core.parser.matcher.statement.parseStatement
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.binding.parameter.parseFunParameters
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.node.TypeReferenceParser
import puzzle.core.symbol.Modifier

class FunDeclarationParser private constructor(
    private val cursor: PzlTokenCursor
) : PzlParser {

    companion object : PzlParserProvider<FunDeclarationParser>(::FunDeclarationParser)

    context(_: PzlContext)
    fun parse(
        genericSpec: GenericSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): FunDeclaration {
        val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.FUN)
        val (funName, extension) = if (cursor.check(PzlTokenType.DOT)) {
            cursor.retreat()
            val type = TypeReferenceParser.of(cursor).parse()
            if (type.isNullable) {
                cursor.expect(PzlTokenType.DOT, "函数缺少 '.'")
                val funName = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.FUN)
                funName to type
            } else {
                val segments = (type.type as NamedType).segments.toMutableList()
                val funName = segments.removeLast()
                val type = TypeReference(NamedType(segments))
                funName to type
            }
        } else {
            name to null
        }
        val parameters = parseFunParameters(cursor)
        val returnTypes = mutableListOf<TypeReference>()
        if (cursor.match(PzlTokenType.COLON)) {
            do {
                returnTypes += TypeReferenceParser.of(cursor).parse(isSupportedLambdaType = true)
            } while (cursor.match(PzlTokenType.COMMA))
        } else {
            returnTypes += TypeReference(PzlTypes.Unit)
        }
        val statements = if (cursor.match(PzlTokenType.LBRACE)) {
            buildList {
                while (!cursor.match(PzlTokenType.RBRACE)) {
                    this += parseStatement(cursor)
                }
            }
        } else emptyList()
        return FunDeclaration(
            name = funName,
            parameters = parameters,
            modifiers = modifiers,
            returnTypes = returnTypes,
            extension = extension,
            genericSpec = genericSpec,
            contextSpec = contextSpec,
            statements = statements
        )
    }
}