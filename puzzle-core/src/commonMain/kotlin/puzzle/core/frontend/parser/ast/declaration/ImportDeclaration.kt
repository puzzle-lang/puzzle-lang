package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.util.DotStringListSerializer

@Serializable
class ImportDeclaration(
	@Serializable(with = DotStringListSerializer::class)
	val segments: List<String>,
	val alias: Identifier?,
	val scope: ImportScope,
	override val location: SourceLocation,
) : AstNode

enum class ImportScope {
	SINGLE,
	WILDCARD,
	RECURSIVE
}