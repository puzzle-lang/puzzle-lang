package puzzle.core.frontend.parser.parser.declaration

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.model.span
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.declaration.InitDeclaration
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.statement.parseStatements
import puzzle.core.frontend.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseInitDeclaration(
	header: DeclarationHeader,
	start: SourceLocation,
): InitDeclaration {
	cursor.expect(LBRACE, "init 初始化块缺少 '{'")
	val body = parseStatements()
	val end = cursor.previous.location
	return InitDeclaration(header.docComment, body, start span end)
}