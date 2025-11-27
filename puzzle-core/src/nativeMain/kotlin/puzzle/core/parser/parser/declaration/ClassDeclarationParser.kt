package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.matcher.declaration.member.parseMemberDeclaration
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.binding.parameter.parseClassParameters
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.symbol.Modifier

class ClassDeclarationParser private constructor(
    private val cursor: PzlTokenCursor,
) : PzlParser {

    companion object : PzlParserProvider<ClassDeclarationParser>(::ClassDeclarationParser)

    context(_: PzlContext)
    fun parse(
        genericSpec: GenericSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>
    ): ClassDeclaration {
        val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.CLASS)
        val constructorModifiers = parseModifiers(cursor)
        constructorModifiers.check(cursor, ModifierTarget.CONSTRUCTOR_FUN)
        val parameters = parseClassParameters(cursor)
        val superTypes = parseSuperTypes(cursor)
        val members = if (cursor.match(PzlTokenType.LBRACE)) {
            buildList {
                while (!cursor.match(PzlTokenType.RBRACE)) {
                    this += parseMemberDeclaration(cursor)
                }
            }
        } else emptyList()
        return ClassDeclaration(
            name = name,
            modifiers = modifiers,
            constructorModifiers = constructorModifiers,
            parameters = parameters,
            superTypes = superTypes,
            genericSpec = genericSpec,
            contextSpec = contextSpec,
            members = members
        )
    }
}