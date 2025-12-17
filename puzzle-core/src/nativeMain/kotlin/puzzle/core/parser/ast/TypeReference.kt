package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.model.SourceLocation
import puzzle.core.util.DotStringListSerializer

@Serializable
class TypeReference(
	val type: Type,
	override val location: SourceLocation,
	val isNullable: Boolean = false,
) : AstNode

@Serializable
sealed interface Type : AstNode

@Serializable
class NamedType(
	@Serializable(with = DotStringListSerializer::class)
	val segments: List<String>,
	override val location: SourceLocation,
	val typeArguments: List<TypeArgument> = emptyList(),
) : Type

@Serializable
class LambdaType(
	val parameters: List<Parameter>,
	val returnTypes: List<TypeReference>,
	override val location: SourceLocation,
) : Type