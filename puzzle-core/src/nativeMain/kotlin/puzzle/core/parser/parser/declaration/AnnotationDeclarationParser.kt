package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.parser.binding.parameter.parseAnnotationParameters
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationDeclaration(
    typeSpec: TypeSpec?,
    modifiers: List<Modifier>
): AnnotationDeclaration {
    val name = parseIdentifierName(IdentifierNameTarget.ANNOTATION)
    val parameters = parseAnnotationParameters()
    if (cursor.match(PzlTokenType.LBRACE)) {
        syntaxError("注解不支持 '{'", cursor.previous)
    }
    return AnnotationDeclaration(
        name = name,
        modifiers = modifiers,
        parameters = parameters,
        typeSpec = typeSpec,
    )
}