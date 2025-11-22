package puzzle.core.lexer

import kotlinx.serialization.Serializable
import puzzle.core.PzlContext

@Serializable
data class PzlToken(
	val type: PzlTokenType,
	val value: String,
	val start: Int,
	val end: Int,
	val line: Int,
	val column: Int,
	val endLine: Int = line
) {
	
	val length: Int
		get() = this.end - this.start
}

context(context: PzlContext)
fun List<PzlToken>.formatToString(): String {
	val lineColumns = this.map { "${context.sourcePath}:${it.line}:${it.column}" }
	val max = lineColumns.maxOf { it.length }
	val sb = StringBuilder()
	this.forEachIndexed { index, token ->
		val lineColumn = lineColumns[index]
		sb.append(lineColumn)
		sb.append(" ".repeat(max - lineColumn.length + 2))
		if (token.type == PzlTokenType.STRING || token.value.isNotEmpty()) {
			sb.append("${token.type}(\"${token.value}\")")
		} else {
			sb.append(token.type)
		}
		sb.append("\n")
	}
	return sb.toString()
}

fun List<PzlToken>.removeComments(): List<PzlToken> {
	return this.filter { it.type != PzlTokenType.SINGLE_LINE_COMMENT && it.type != PzlTokenType.MULTI_LINE_COMMENT }
}