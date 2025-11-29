package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.util.DotSeparatedListSerializer

@Serializable
class ImportDeclaration(
	@Serializable(with = DotSeparatedListSerializer::class)
	val segments: List<String>,
	val alias: String?,
	val scope: ImportScope
) : Declaration

enum class ImportScope {
	SINGLE,
	WILDCARD,
	RECURSIVE
}