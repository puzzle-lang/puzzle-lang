package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.token.kinds.NumberLiteralType
import puzzle.core.token.kinds.NumberSystem

@Serializable
sealed interface LiteralExpression : Expression

@Serializable
class NumberLiteral(
	val value: String,
	val system: NumberSystem,
	val type: NumberLiteralType,
	override val location: SourceLocation,
) : LiteralExpression

@Serializable
class StringLiteral(
	val value: String,
	override val location: SourceLocation,
) : LiteralExpression

@Serializable
class BooleanLiteral(
	val value: Boolean,
	override val location: SourceLocation,
) : LiteralExpression

@Serializable
class CharLiteral(
	val value: String,
	override val location: SourceLocation,
) : LiteralExpression

@Serializable
class NullLiteral(
	override val location: SourceLocation,
) : LiteralExpression