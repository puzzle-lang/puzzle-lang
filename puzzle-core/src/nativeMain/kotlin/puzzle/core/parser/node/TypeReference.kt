package puzzle.core.parser.node

import kotlinx.serialization.Serializable
import puzzle.core.parser.parameter.Parameter

@Serializable
data class TypeReference(
	val type: Type,
	val isNullable: Boolean = false,
	val isArray: Boolean = false
)

@Serializable
sealed interface Type

@Serializable
data class NamedType(
	val segments: List<String>
) : Type {
	
	companion object {
		
		fun parse(qualifiedName: String): NamedType {
			return NamedType(qualifiedName.split("."))
		}
	}
}

@Serializable
data class LambdaType(
	val parameters: List<Parameter>,
	val returnTypes: List<TypeReference>
) : Type