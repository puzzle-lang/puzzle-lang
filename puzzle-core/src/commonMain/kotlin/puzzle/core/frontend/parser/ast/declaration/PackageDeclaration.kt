package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.util.DotStringListSerializer

@Serializable
class PackageDeclaration(
	@Serializable(with = DotStringListSerializer::class)
	val segments: List<String>,
	override val location: SourceLocation,
) : AstNode