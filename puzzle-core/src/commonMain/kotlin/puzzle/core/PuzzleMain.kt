@file:OptIn(ExperimentalSerializationApi::class)

package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import puzzle.core.io.readFileChars
import puzzle.core.io.resolveAbsolutePath
import puzzle.core.lexer.PzlLexer
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.PzlProgram
import puzzle.core.parser.parser.parseSourceFileNode
import puzzle.core.util.getCurrentMemoryUsage
import kotlin.time.measureTimedValue

fun puzzleMain(args: Array<out String>) {
	val value = measureTimedValue {
		val command = args.firstOrNull() ?: return help()
		when (command) {
			"-c", "--compile" -> {
				val paths = args.drop(1)
				if (paths.isEmpty()) {
					println("请至少指定一个 Puzzle 程序文件，使用 puzzle -h 查看使用手册")
					return
				}
				compile(args.drop(1))
			}
			
			"-h", "--help" -> return help()
			
			else -> return unknown()
		}
	}
	val usage = getCurrentMemoryUsage()
	value.value.alsoLog()
	println("执行耗时: ${value.duration}")
	println("内存使用: $usage")
}

private fun compile(paths: List<String>) = runBlocking {
	val jobs = paths.map { path ->
		async(Dispatchers.Default) {
			val source = measureTimedValue { readFileChars(path) }
			val path = resolveAbsolutePath(path)
			println("$path -> 文件读取: ${source.duration} CHARS: ${source.value.size}")
			val lineStarts = source.value.getLineStarts()
			val context = PzlContext(path, lineStarts)
			context(context) {
				val tokens = measureTimedValue { PzlLexer.default(source.value).scan() }
				println("$path -> 词法分析: ${tokens.duration} TOKENS: ${tokens.value.size}")
				val cursor = PzlTokenCursor(tokens.value)
				context(cursor) {
					val node = measureTimedValue { parseSourceFileNode() }
					println("$path -> 语法分析: ${node.duration}")
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

val json = Json {
	prettyPrint = true
	encodeDefaults = false
	classDiscriminator = "class"
	ignoreUnknownKeys = true
	explicitNulls = false
}

inline fun <reified T> T.alsoLog(): T {
	if (this == null) return this
	println(json.encodeToString(this))
	return this
}

private fun help() {
	val help = """
		使用方式:
            puzzle -c --compile <File1.pzl> [File2.pzl, File3.pzl, ...]     编译 Puzzle (.pzl) 程序文件
            puzzle -h --help                                                查看使用手册
	""".trimIndent()
	println(help)
}

private fun unknown() {
	println("未知命令，请使用: puzzle -h 或 puzzle --help 查看使用帮助")
}