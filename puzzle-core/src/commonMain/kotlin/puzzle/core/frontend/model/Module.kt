package puzzle.core.frontend.model

import kotlinx.serialization.Serializable
import puzzle.core.frontend.parser.ast.SourceFileNode

@Serializable
class Module(
	val name: String,
	val nodes: List<SourceFileNode>,
)