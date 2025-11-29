package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode
import puzzle.core.util.DotSeparatedListSerializer

@Serializable
class PackageDeclaration(
	@Serializable(with = DotSeparatedListSerializer::class)
	val segments: List<String>,
) : AstNode