package puzzle.core

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.writeString
import puzzle.core.frontend.model.Project
import puzzle.core.frontend.processProject
import puzzle.core.util.*
import kotlin.time.measureTimedValue

fun main(args: Array<out String>) {
	var projectPath: Path
	val value = measureTimedValue {
		val command = args.firstOrNull() ?: return help()
		when (command) {
			"build" -> {
				projectPath = args.drop(1).firstOrNull()
					?.let { Path(it) }
					?: return println("缺少项目路径, 使用 puzzle help 查看使用手册")
				build(projectPath)
			}
			
			"help" -> return help()
			
			"version" -> return version()
			
			else -> return unknown()
		}
	}
	val usage = getCurrentMemoryUsage()
	println("执行用时: ${value.duration}")
	println("内存使用: $usage")
	saveToBuild(projectPath, value.value)
	println("AST 保存完成")
}

private fun build(projectPath: Path): Project {
	return processProject(projectPath)
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
	println("当前 Puzzle 版本: 0.1.1, Kotlin 版本: 2.3.0")
}

private fun unknown() {
	println("未知命令, 请使用: puzzle -h 或 puzzle --help 查看使用帮助")
}

private fun saveToBuild(projectPath: Path, project: Project) {
	val projectPath = projectPath.absolutePath
	val buildPath = Path(projectPath, "build", "ast")
	if (buildPath.exists()) {
		buildPath.delete()
	}
	project.modules.forEach { module ->
		module.nodes.forEach { node ->
			val path = node.path.removePrefix(projectPath).removeSuffix(".pzl")
			val astPath = Path(buildPath, "$path.json")
			if (astPath.parent == null) return@forEach
			if (!astPath.parent!!.exists()) {
				astPath.parent!!.createDirectories()
			}
			astPath.sink().buffered().use {
				it.writeString(json.encodeToString(node))
			}
		}
	}
}