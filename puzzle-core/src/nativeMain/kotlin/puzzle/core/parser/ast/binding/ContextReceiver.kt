package puzzle.core.parser.ast.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.node.TypeReference

@Serializable
data class ContextReceiver(
	val name: String,
	val type: TypeReference
)