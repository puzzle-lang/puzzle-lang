package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.util.DotSeparatedListSerializer

@Serializable
data class TypeReference(
	val type: Type,
	val isNullable: Boolean = false
)

@Serializable
sealed interface Type

@Serializable
data class NamedType(
	@Serializable(with = DotSeparatedListSerializer::class)
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