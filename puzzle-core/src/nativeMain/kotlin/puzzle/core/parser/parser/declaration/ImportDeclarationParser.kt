package puzzle.core.parser.parser.declaration

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.ast.declaration.ImportScope
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.matchIdentifierName
import puzzle.core.parser.parser.identifier.parseIdentifierName

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseImportDeclaration(): ImportDeclaration {
    val name = parseIdentifierName(IdentifierNameTarget.IMPORT)
    val paths = mutableListOf(name)
    var scope = ImportScope.SINGLE
    var alias: String? = null
    while (cursor.match(PzlTokenType.DOT)) {
        when {
            matchIdentifierName() -> {
                paths += cursor.previous.value
                if (cursor.match(PzlTokenType.AS)) {
                    alias = parseIdentifierName(IdentifierNameTarget.IMPORT_AS)
                    break
                }
            }

            cursor.match(PzlTokenType.STAR) -> {
                scope = ImportScope.WILDCARD
                break
            }

            cursor.match(PzlTokenType.DOUBLE_STAR) -> {
                scope = ImportScope.RECURSIVE
                break
            }
        }
    }
    return ImportDeclaration(
        segments = paths,
        alias = alias,
        scope = scope
    )
}