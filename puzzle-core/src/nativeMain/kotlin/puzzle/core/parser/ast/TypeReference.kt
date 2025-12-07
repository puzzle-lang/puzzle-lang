package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.util.DotSeparatedListSerializer

@Serializable
class TypeReference(
	val type: Type,
	val isNullable: Boolean = false
)

@Serializable
sealed interface Type

@Serializable
class NamedType(
	@Serializable(with = DotSeparatedListSerializer::class)
	val segments: List<String>,
	val typeArguments: List<TypeArgument> = emptyList()
) : Type {
	
	companion object {
		
		fun parse(qualifiedName: String): NamedType {
			return NamedType(qualifiedName.split("."))
		}
	}
}

@Serializable
class LambdaType(
	val parameters: List<Parameter>,
	val returnTypes: List<TypeReference>
) : Type