package puzzle.core.frontend.parser.ast

import puzzle.core.frontend.model.SourceLocation

interface AstNode {
	
	val location: SourceLocation
}