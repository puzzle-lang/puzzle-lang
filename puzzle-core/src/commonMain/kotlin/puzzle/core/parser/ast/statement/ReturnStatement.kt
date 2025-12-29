package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.expression.ExpressionSpec
import puzzle.core.parser.ast.expression.Identifier

@Serializable
class ReturnStatement(
	val label: Identifier?,
	val expression: ExpressionSpec?,
	override val location: SourceLocation,
) : Statement