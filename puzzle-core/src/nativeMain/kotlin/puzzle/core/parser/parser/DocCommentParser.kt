package puzzle.core.parser.parser

import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.DocComment
import puzzle.core.parser.ast.TokenRange
import puzzle.core.token.CommentKind

context(cursor: PzlTokenCursor)
fun parseDocComment(): DocComment? {
	return if (cursor.match<CommentKind.Doc>()) {
		val token = cursor.previous
		val range = TokenRange(token.start, token.end)
		return DocComment(token.value, range)
	} else null
}