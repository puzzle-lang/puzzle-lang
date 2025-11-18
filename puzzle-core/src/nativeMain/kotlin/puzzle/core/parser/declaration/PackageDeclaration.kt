package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.node.AstNode

@Serializable
data class PackageDeclaration(
	val paths: List<String>,
) : AstNode