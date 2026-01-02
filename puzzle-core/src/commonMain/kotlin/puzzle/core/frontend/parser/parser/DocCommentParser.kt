package puzzle.core.frontend.parser.parser

import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.DocComment
import puzzle.core.frontend.token.kinds.CommentKind.Doc

context(cursor: PzlTokenCursor)
fun parseDocComment(): DocComment? {
	return if (cursor.match { it.kind is Doc }) {
		val token = cursor.previous
		DocComment(token.value, token.location)
	} else null
}