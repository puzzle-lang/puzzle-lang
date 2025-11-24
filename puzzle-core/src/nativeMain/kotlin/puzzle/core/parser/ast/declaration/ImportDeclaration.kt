package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable

@Serializable
data class ImportDeclaration(
	val paths: List<String>,
	val alias: String?,
	val scope: ImportScope
) : Declaration

enum class ImportScope {
	SINGLE,
	WILDCARD,
	RECURSIVE
}