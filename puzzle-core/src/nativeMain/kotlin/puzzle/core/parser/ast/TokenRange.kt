package puzzle.core.parser.ast

import kotlinx.serialization.Serializable

@Serializable
data class TokenRange(
	val start: Int,
	val end: Int
)