package puzzle.core

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import puzzle.core.io.File
import puzzle.core.lexer.PzlLexer
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.SourceFileNodeParser
import puzzle.core.parser.node.PzlProgram
import puzzle.core.util.currentMemoryUsage
import kotlin.native.runtime.NativeRuntimeApi
import kotlin.time.measureTime

@OptIn(ExperimentalForeignApi::class, NativeRuntimeApi::class)
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
	println("内存使用：${currentMemoryUsage()}")
}

private fun run(paths: List<String>) = runBlocking {
	val files = paths.toSet().map(::File).distinctBy { it.absolutePath }
	val jobs = files.map { file ->
		async(Dispatchers.Default) {
			val context = PzlContext(file.absolutePath)
			context(context) {
				val input = file.readText().toCharArray()
				val rawTokens = PzlLexer(input).lex()
				val cursor = PzlTokenCursor(rawTokens)
				SourceFileNodeParser.of(cursor).parse()
			}
		}
	}
	val sourceFileNodes = jobs.awaitAll()
	val program = PzlProgram(sourceFileNodes)
//	println(json.encodeToString(program))
}

class PzlContext(
	val sourcePath: String
)

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