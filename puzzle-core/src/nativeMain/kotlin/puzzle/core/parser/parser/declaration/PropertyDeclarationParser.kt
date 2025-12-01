package puzzle.core.parser.parser.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.matcher.expression.parseExpressionChain
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseExtensionAndIdentifierName
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePropertyDeclaration(
    typeSpec: TypeSpec?,
    contextSpec: ContextSpec?,
    modifiers: List<Modifier>
): PropertyDeclaration {
    val (extension, name) = parseExtensionAndIdentifierName(IdentifierNameTarget.PROPERTY)
    val type = if (cursor.match(PzlTokenType.COLON)) parseTypeReference(isSupportedLambdaType = true) else null
    val initialize = if (cursor.match(PzlTokenType.ASSIGN)) parseExpressionChain() else null
    return PropertyDeclaration(
        name = name,
        type = type,
        modifiers = modifiers,
        extension = extension,
        typeSpec = typeSpec,
        contextSpec = contextSpec,
        initializer = initialize
    )
}