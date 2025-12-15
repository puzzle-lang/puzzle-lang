package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import puzzle.core.io.File
import puzzle.core.lexer.PzlLexer
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.PzlProgram
import puzzle.core.parser.parser.parseSourceFileNode
import puzzle.core.util.currentMemoryUsage
import kotlin.native.runtime.NativeRuntimeApi
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@OptIn(NativeRuntimeApi::class, ExperimentalStdlibApi::class)
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
			val source = measureTimedValue { file.readChars() }
			println("${file.absolutePath} -> 文件读取: ${source.duration}")
			val lineStarts = source.value.getLineStarts()
			val context = PzlContext(file.absolutePath, lineStarts)
			context(context) {
				val tokens = measureTimedValue { PzlLexer(source.value).lex() }
				println("${file.absolutePath} -> 词法分析: ${tokens.duration}")
				val cursor = PzlTokenCursor(tokens.value)
				context(cursor) {
					val node = measureTimedValue { parseSourceFileNode() }
					println("${file.absolutePath} -> 语法分析: ${node.duration}")
					node.value
				}
			}
		}
	}
	val sourceFileNodes = jobs.awaitAll()
	PzlProgram(sourceFileNodes).alsoLog()
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
	ignoreUnknownKeys = true
}

inline fun <reified T> T.alsoLog(): T {
	val str = this?.let {
		val value = measureTimedValue {
			json.encodeToString(it)
		}
		println("编码耗时: ${value.duration}")
		value.value
	} ?: return this
	println(str)
	val duration = measureTime {
		json.decodeFromString<T>(str)
	}
	println("解码耗时: $duration")
	return this
}

private fun help() {
	val help = """
		使用方式:
            puzzle -c <main.pzl> [file1.pzl, file2.pzl, ...]  编译 Puzzle (.pzl) 程序文件
            puzzle -h                                         查看使用手册
	""".trimIndent()
	println(help)
}