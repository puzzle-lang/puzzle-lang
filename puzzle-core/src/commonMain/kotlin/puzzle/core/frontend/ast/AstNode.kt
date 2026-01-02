package puzzle.core.frontend.ast

import puzzle.core.frontend.model.SourceLocation

interface AstNode {
	
	val location: SourceLocation
}