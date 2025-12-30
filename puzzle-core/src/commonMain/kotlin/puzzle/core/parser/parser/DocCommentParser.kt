package puzzle.core.parser.parser

import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.DocComment
import puzzle.core.token.kinds.CommentKind.Doc

context(cursor: PzlTokenCursor)
fun parseDocComment(): DocComment? {
	return if (cursor.match { it is Doc }) {
		val token = cursor.previous
		DocComment(token.value, token.location)
	} else null
}