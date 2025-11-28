package puzzle.core.parser.ast.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TypeReference

@Serializable
data class ContextReceiver(
	val name: String,
	val type: TypeReference
)

@Serializable
data class ContextSpec(
	val receivers: List<ContextReceiver>,
	val isInherited: Boolean
)