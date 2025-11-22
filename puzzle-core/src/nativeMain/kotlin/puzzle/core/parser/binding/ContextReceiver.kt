package puzzle.core.parser.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.node.TypeReference

@Serializable
data class ContextReceiver(
	val name: String?,
	val type: TypeReference
)