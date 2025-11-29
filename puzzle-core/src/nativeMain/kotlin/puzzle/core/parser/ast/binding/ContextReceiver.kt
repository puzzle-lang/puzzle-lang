package puzzle.core.parser.ast.binding

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TypeReference

@Serializable
class ContextReceiver(
	val name: String,
	val type: TypeReference
)

@Serializable
class ContextSpec(
	val receivers: List<ContextReceiver>,
	val isInherited: Boolean
)