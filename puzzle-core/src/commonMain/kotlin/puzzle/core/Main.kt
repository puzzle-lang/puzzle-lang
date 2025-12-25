@file:OptIn(ExperimentalSerializationApi::class)

package puzzle.core

import kotlinx.serialization.ExperimentalSerializationApi
import puzzle.core.util.alsoLog
import puzzle.core.util.getCurrentMemoryUsage
import kotlin.time.measureTimedValue

fun main(args: Array<out String>) {
	val value = measureTimedValue {
		val command = args.firstOrNull() ?: return help()
		when (command) {
			"-c", "--compile" -> {
				val paths = args.drop(1)
				if (paths.isEmpty()) {
					return println("请至少指定一个 Puzzle 程序文件，使用 puzzle -h 查看使用手册")
				}
				compile(paths)
			}
			
			"-h", "--help" -> return help()
			
			"-v", "--version" -> return version()
			
			else -> return unknown()
		}
	}
	val usage = getCurrentMemoryUsage()
	value.value.alsoLog()
	println("执行耗时: ${value.duration}")
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
	println("未知命令，请使用: puzzle -h 或 puzzle --help 查看使用帮助")
}