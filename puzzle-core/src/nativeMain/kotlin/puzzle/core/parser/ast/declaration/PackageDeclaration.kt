package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode

@Serializable
data class PackageDeclaration(
	val paths: List<String>,
) : AstNode