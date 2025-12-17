package puzzle.core.parser.ast

import puzzle.core.token.SourceLocation

interface AstNode {
	
	val location: SourceLocation
}