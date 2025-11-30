package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import puzzle.core.io.File
import puzzle.core.lexer.PzlLexer
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.PzlProgram
import puzzle.core.parser.parser.SourceFileNodeParser
import puzzle.core.util.currentMemoryUsage
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

fun main(vararg args: String) {
	val duration = measureTime {
		val command = args.firstOrNull() ?: run {
			help()
			return
		}
		when (command) {
			"-c" -> {
				val paths = args.drop(1)
				if (paths.isEmpty()) {
					println("请至少指定一个 Puzzle 程序文件，使用 puzzle -h 查看使用手册")
					return
				}
				run(args.drop(1))
			}
			
			else -> help()
		}
	}
	println("执行耗时: $duration")
	println("内存使用: ${currentMemoryUsage()}")
}

private fun run(paths: List<String>) = runBlocking {
	val files = paths.toSet().map(::File).distinctBy { it.absolutePath }
	val jobs = files.map { file ->
		async(Dispatchers.Default) {
			val source = measureTimedValue {
				file.readText().toCharArray()
			}
			println("READ: ${source.duration}")
			val lineStarts = source.value.getLineStarts()
			val context = PzlContext(file.absolutePath, lineStarts)
			context(context) {
				val rawTokens = measureTimedValue {
					PzlLexer(source.value).lex()
				}
				println("LEX: ${rawTokens.duration}")
				val cursor = PzlTokenCursor(rawTokens.value)
				val node = measureTimedValue {
					SourceFileNodeParser.of(cursor).parse()
				}
				println("PARSE: ${node.duration}")
				node.value
			}
		}
	}
	val sourceFileNodes = jobs.awaitAll()
	val program = PzlProgram(sourceFileNodes)
//	println(json.encodeToString(program))
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
	encodeDefaults = true
	classDiscriminator = "class"
}

private fun help() {
	val help = """
		使用方式:
            puzzle -c <main.pzl> [file1.pzl, file2.pzl, ...]  编译 Puzzle (.pzl) 程序文件
            puzzle -h                                         查看使用手册
	""".trimIndent()
	println(help)
}