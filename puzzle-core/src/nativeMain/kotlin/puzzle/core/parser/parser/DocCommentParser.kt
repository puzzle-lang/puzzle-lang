package puzzle.core.parser.parser

import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.DocComment

context(cursor: PzlTokenCursor)
fun parseDocComment(): DocComment? {
	return if (cursor.match<puzzle.core.token.kinds.CommentKind.Doc>()) {
		val token = cursor.previous
		DocComment(token.value, token.location)
	} else null
}