package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.ast.parameter.ParameterReference
import puzzle.core.frontend.ast.statement.Statement

@Serializable
class LambdaExpression(
	val label: Identifier?,
	val references: List<ParameterReference>,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Expression, CompoundAssignable