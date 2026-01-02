package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.parameter.ParameterReference
import puzzle.core.frontend.parser.ast.statement.Statement

@Serializable
class LambdaExpression(
	val label: Identifier?,
	val references: List<ParameterReference>,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Expression, CompoundAssignable