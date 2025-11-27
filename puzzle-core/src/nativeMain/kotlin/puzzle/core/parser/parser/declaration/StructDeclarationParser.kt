package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.matcher.declaration.member.parseMemberDeclaration
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.binding.parameter.parseStructParameters
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.symbol.Modifier

class StructDeclarationParser private constructor(
    private val cursor: PzlTokenCursor
) : PzlParser {

    companion object : PzlParserProvider<StructDeclarationParser>(::StructDeclarationParser)

    context(_: PzlContext)
    fun parse(
        genericSpec: GenericSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): StructDeclaration {
        val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.STRUCT)
        val parameters = parseStructParameters(cursor)
        val members = if (cursor.match(PzlTokenType.LBRACE)) {
            buildList {
                while (!cursor.match(PzlTokenType.RBRACE)) {
                    this += parseMemberDeclaration(cursor)
                }
            }
        } else emptyList()
        return StructDeclaration(
            name = name,
            modifiers = modifiers,
            parameters = parameters,
            genericSpec = genericSpec,
            contextSpec = contextSpec,
            members = members
        )
    }
}