package puzzle.core.parser.parser.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.matcher.declaration.parseMemberDeclaration
import puzzle.core.parser.parser.binding.parameter.parseClassParameters
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseClassDeclaration(
    typeSpec: TypeSpec?,
    contextSpec: ContextSpec?,
    modifiers: List<Modifier>
): ClassDeclaration {
    val name = parseIdentifierName(IdentifierNameTarget.CLASS)
    val constructorModifiers = parseModifiers()
    constructorModifiers.check(ModifierTarget.CONSTRUCTOR_FUN)
    val parameters = parseClassParameters()
    val superTypes = parseSuperTypes()
    val members = if (cursor.match(PzlTokenType.LBRACE)) {
        buildList {
            while (!cursor.match(PzlTokenType.RBRACE)) {
                this += parseMemberDeclaration()
            }
        }
    } else emptyList()
    return ClassDeclaration(
        name = name,
        modifiers = modifiers,
        constructorModifiers = constructorModifiers,
        parameters = parameters,
        superTypes = superTypes,
        typeSpec = typeSpec,
        contextSpec = contextSpec,
        members = members
    )
}