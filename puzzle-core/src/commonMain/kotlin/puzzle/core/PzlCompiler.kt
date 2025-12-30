package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import puzzle.core.io.readFileChars
import puzzle.core.io.resolveAbsolutePath
import puzzle.core.lexer.PzlLexer
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.PzlProgram
import puzzle.core.parser.parser.parseSourceFileNode
import kotlin.time.measureTimedValue

fun compile(
	paths: List<String>,
	statistics: MutableMap<String, PzlStatistic>,
): PzlProgram = runBlocking {
	val jobs = paths.map { path ->
		async(Dispatchers.Default) {
			val source = measureTimedValue { readFileChars(path) }
			val path = resolveAbsolutePath(path)
			val lineStarts = source.value.getLineStarts()
			val context = PzlContext(path, lineStarts)
			context(context) {
				val tokens = measureTimedValue { PzlLexer.default(source.value).scan() }
				val cursor = PzlTokenCursor(tokens.value)
				context(cursor) {
					val node = measureTimedValue { parseSourceFileNode() }
					statistics[path] = PzlStatistic(
						charSize = source.value.size,
						tokenSize = tokens.value.size,
						readDuration = source.duration,
						lexerDuration = tokens.duration,
						parserDuration = node.duration
					)
					node.value
				}
			}
		}
	}
	val sourceFileNodes = jobs.awaitAll()
	PzlProgram(sourceFileNodes)
}

private fun CharArray.getLineStarts(): IntArray {
	val starts = mutableListOf(0)
	this.forEachIndexed { index, char ->
		if (char == '\n' && index + 1 < this.size) {
			starts += index + 1
		}
	}
	return starts.toIntArray()
}