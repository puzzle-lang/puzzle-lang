package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.parameter.LambdaParameterReference
import puzzle.core.parser.ast.statement.Statement

@Serializable
class LambdaExpression(
	val label: Identifier?,
	val references: List<LambdaParameterReference>,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Expression