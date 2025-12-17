package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.model.SourceLocation
import puzzle.core.util.DotStringListSerializer

@Serializable
class ImportDeclaration(
	@Serializable(with = DotStringListSerializer::class)
	val segments: List<String>,
	val alias: IdentifierExpression?,
	val scope: ImportScope,
	override val location: SourceLocation,
) : Declaration

enum class ImportScope {
	SINGLE,
	WILDCARD,
	RECURSIVE
}