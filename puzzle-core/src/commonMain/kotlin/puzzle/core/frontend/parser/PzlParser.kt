package puzzle.core.frontend.parser

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.parser.parseFile

object PzlParser {
	
	context(_: PzlContext)
	fun parse(cursor: PzlTokenCursor): AstFile {
		return context(cursor) {
			parseFile()
		}
	}
}