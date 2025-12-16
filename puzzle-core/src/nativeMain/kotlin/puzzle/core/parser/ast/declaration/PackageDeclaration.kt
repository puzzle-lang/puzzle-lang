package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode
import puzzle.core.util.DotStringListSerializer

@Serializable
class PackageDeclaration(
	@Serializable(with = DotStringListSerializer::class)
	val segments: List<String>,
) : AstNode