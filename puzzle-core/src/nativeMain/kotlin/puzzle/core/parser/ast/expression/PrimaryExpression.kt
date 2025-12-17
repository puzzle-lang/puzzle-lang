package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.token.SourceLocation
import puzzle.core.token.kinds.NumberLiteralType
import puzzle.core.token.kinds.NumberSystem

@Serializable
class NumberLiteral(
	val value: String,
	val system: NumberSystem,
	val type: NumberLiteralType,
	override val location: SourceLocation,
) : Expression

@Serializable
class StringLiteral(
	val value: String,
	override val location: SourceLocation,
) : Expression

@Serializable
class BooleanLiteral(
	val value: Boolean,
	override val location: SourceLocation,
) : Expression

@Serializable
class CharLiteral(
	val value: String,
	override val location: SourceLocation,
) : Expression

@Serializable
class ThisLiteral(
	override val location: SourceLocation,
) : Expression

@Serializable
class SuperLiteral(
	override val location: SourceLocation,
) : Expression

@Serializable
class NullLiteral(
	override val location: SourceLocation,
) : Expression