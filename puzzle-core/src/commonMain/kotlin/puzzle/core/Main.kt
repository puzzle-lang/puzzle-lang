package puzzle.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import puzzle.core.frontend.ast.AstJsonWriter
import puzzle.core.frontend.processProject
import puzzle.core.util.getCurrentMemoryUsage
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

fun main(args: Array<out String>) {
	val command = args.firstOrNull() ?: return help()
	when (command) {
		"build" -> {
			val projectDir = args.drop(1).firstOrNull()
				?: return println("缺少项目路径, 使用 puzzle help 查看使用手册")
			val projectPath = Path(projectDir)
			build(projectPath)
		}
		
		"help" -> help()
		"version" -> version()
		else -> unknown()
	}
}

private fun build(projectPath: Path) = runBlocking(Dispatchers.Default) {
	val value = measureTimedValue { processProject(projectPath) }
	val usage = getCurrentMemoryUsage()
	println("执行用时: ${value.duration}")
	println("内存使用: $usage")
	val duration = measureTime { AstJsonWriter.write(projectPath, value.value) }
	println("AST已保存: $duration")
}

private fun help() {
	val message = """
        ╭───────────────────────────────┬───────────────────────────────────╮
        │ Puzzle CLI                    │ Usage Information                 │
        ├───────────────────────────────┼───────────────────────────────────┤
        │ puzzle build <project-path>   │ Build the Puzzle project          │
        │ puzzle version                │ Show Puzzle version information   │
        │ puzzle help                   │ Show this help message            │
        ╰───────────────────────────────┴───────────────────────────────────╯
    """.trimIndent()
	println(message)
}

private fun version() {
	val message = """
        ╭───────────────────────────┬──────────────╮
        │ Puzzle CLI                │ v0.1.2-dev   │
        ├───────────────────────────┼──────────────┤
        │ kotlin                    │ v2.3.0       │
        │ kotlinx-coroutines-core   │ v1.10.2      │
        │ kotlinx-serialization     │ v1.10-0-RC   │
        │ kotlinx-io-core           │ v0.8.2       │
        ╰───────────────────────────┴──────────────╯
    """.trimIndent()
	println(message)
}

private fun unknown() {
	println("未知命令, 请使用: puzzle -h 或 puzzle --help 查看使用帮助")
}