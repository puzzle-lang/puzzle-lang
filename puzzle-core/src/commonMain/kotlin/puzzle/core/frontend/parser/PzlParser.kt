package puzzle.core.frontend.parser

import puzzle.core.frontend.ast.SourceFileNode
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.parser.parseSourceFileNode

object PzlParser {
	
	context(_: PzlContext)
	fun parse(cursor: PzlTokenCursor): SourceFileNode {
		return context(cursor) {
			parseSourceFileNode()
		}
	}
}