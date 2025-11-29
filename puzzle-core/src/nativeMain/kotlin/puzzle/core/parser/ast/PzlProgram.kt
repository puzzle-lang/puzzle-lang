package puzzle.core.parser.ast

import kotlinx.serialization.Serializable

@Serializable
class PzlProgram(
	val files: List<SourceFileNode>
)