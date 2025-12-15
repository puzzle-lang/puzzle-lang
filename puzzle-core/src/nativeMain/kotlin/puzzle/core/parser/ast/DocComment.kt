package puzzle.core.parser.ast

import kotlinx.serialization.Serializable

@Serializable
class DocComment(
	val value: String,
	val range: TokenRange
)