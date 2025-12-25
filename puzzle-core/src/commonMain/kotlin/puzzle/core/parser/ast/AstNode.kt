package puzzle.core.parser.ast

import puzzle.core.model.SourceLocation

interface AstNode {
	
	val location: SourceLocation
}