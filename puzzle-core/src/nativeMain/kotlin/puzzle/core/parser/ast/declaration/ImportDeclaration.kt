package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.util.DotStringListSerializer

@Serializable
class ImportDeclaration(
	@Serializable(with = DotStringListSerializer::class)
	val qualifiedName: List<String>,
	val alias: String?,
	val scope: ImportScope
) : Declaration

enum class ImportScope {
	SINGLE,
	WILDCARD,
	RECURSIVE
}