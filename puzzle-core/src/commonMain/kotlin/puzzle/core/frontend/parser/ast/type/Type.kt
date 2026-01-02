package puzzle.core.frontend.parser.ast.type

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.argument.TypeArgument
import puzzle.core.frontend.parser.ast.declaration.ReturnSpec
import puzzle.core.frontend.parser.ast.parameter.LambdaContextSpec
import puzzle.core.frontend.parser.ast.parameter.LambdaParameter
import puzzle.core.util.DotStringListSerializer

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
	val extension: TypeReference?,
	val contextSpec: LambdaContextSpec?,
	val parameters: List<LambdaParameter>,
	val returnSpec: ReturnSpec,
	override val location: SourceLocation,
) : Type