package puzzle.core

import puzzle.core.util.alsoLog
import puzzle.core.util.getCurrentMemoryUsage
import kotlin.time.Duration
import kotlin.time.measureTimedValue

fun main(args: Array<out String>) {
	val statistics = mutableMapOf<String, PzlStatistic>()
	val value = measureTimedValue {
		val command = args.firstOrNull() ?: return help()
		when (command) {
			"-c", "--compile" -> {
				val paths = args.drop(1)
				if (paths.isEmpty()) {
					return println("请至少指定一个 Puzzle 程序文件, 使用 puzzle -h 查看使用手册")
				}
				compile(paths, statistics)
			}
			
			"-h", "--help" -> return help()
			
			"-v", "--version" -> return version()
			
			else -> return unknown()
		}
	}
	val usage = getCurrentMemoryUsage()
	value.value.alsoLog()
	statistics.forEach { (path, statistic) ->
		println(path)
		println("字符数量: ${statistic.charSize}")
		println("符号数量: ${statistic.tokenSize}")
		println("读取用时: ${statistic.readDuration}")
		println("词法用时: ${statistic.lexerDuration} 速度: ${statistic.lexerSpeed} chars/ms")
		println("语法用时: ${statistic.parserDuration} 速度: ${statistic.parserSpeed} tokens/ms")
		println("总计用时: ${statistic.duration}")
	}
	println("=".repeat(100))
	println("执行用时: ${value.duration}")
	println("内存使用: $usage")
}

private fun help() {
	val help = """
		使用方式:
            puzzle -c --compile Main.pzl [File1.pzl, File2.pzl, ...]    编译 Puzzle (.pzl) 程序文件
			puzzle -v --version                                         查看 Puzzle 版本信息
            puzzle -h --help                                            查看 Puzzle 使用帮助
	""".trimIndent()
	println(help)
}

private fun version() {
	println("当前 Puzzle 版本: 0.1.0, Kotlin 版本: 2.3.0")
}

private fun unknown() {
	println("未知命令, 请使用: puzzle -h 或 puzzle --help 查看使用帮助")
}

class PzlStatistic(
	val charSize: Int,
	val tokenSize: Int,
	val readDuration: Duration,
	val lexerDuration: Duration,
	val parserDuration: Duration,
) {
	
	val duration = readDuration + lexerDuration + parserDuration
	
	val lexerSpeed = ((charSize * 1_000_000.0 / lexerDuration.inWholeNanoseconds) * 1_000).toInt() / 1_000.0
	
	val parserSpeed = ((tokenSize * 1_000_000.0 / parserDuration.inWholeNanoseconds) * 1_000).toInt() / 1_000.0
}