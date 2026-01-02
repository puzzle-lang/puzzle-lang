package puzzle.core.frontend.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.expression.Expression
import puzzle.core.frontend.parser.ast.expression.Identifier

@Serializable
class WhileStatement(
	val label: Identifier?,
	val condition: Expression,
	val kind: WhileKind,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Statement

enum class WhileKind {
	WHILE,
	DO_WHILE
}