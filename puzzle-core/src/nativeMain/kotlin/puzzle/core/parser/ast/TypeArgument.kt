package puzzle.core.parser.ast

import kotlinx.serialization.Serializable

@Serializable
class TypeArgument(
	val name: String?,
	val type: TypeReference
)