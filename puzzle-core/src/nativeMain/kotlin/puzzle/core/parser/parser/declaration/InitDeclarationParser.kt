package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.InitDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

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