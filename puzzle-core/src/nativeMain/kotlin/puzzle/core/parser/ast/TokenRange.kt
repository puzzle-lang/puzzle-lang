package puzzle.core.parser.ast

import kotlinx.serialization.Serializable

@Serializable
class TokenRange(
	val start: Int,
	val end: Int
)