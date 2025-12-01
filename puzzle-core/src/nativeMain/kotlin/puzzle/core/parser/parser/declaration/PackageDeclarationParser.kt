package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.PackageDeclaration
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePackageDeclaration(): PackageDeclaration {
    if (!cursor.match(PzlTokenType.PACKAGE)) {
        syntaxError("文件缺少包定义", cursor.current)
    }
    val packages = mutableListOf<String>()
    packages += parseIdentifierName(IdentifierNameTarget.PACKAGE)
    while (cursor.match(PzlTokenType.DOT)) {
        packages += parseIdentifierName(IdentifierNameTarget.PACKAGE_DOT)
    }

    return PackageDeclaration(packages)
}