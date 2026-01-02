package puzzle.core.frontend

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import puzzle.core.frontend.discovery.collectProjectSource
import puzzle.core.frontend.lexer.PzlLexer
import puzzle.core.frontend.model.Module
import puzzle.core.frontend.model.Project
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.SourceFileNode
import puzzle.core.frontend.parser.parser.parseSourceFileNode
import puzzle.core.util.absolutePath
import puzzle.core.util.readText
import kotlin.time.measureTimedValue

fun processProject(projectPath: Path): Project = runBlocking {
	val projectSource = collectProjectSource(projectPath)
	val jobs = projectSource.moduleSources.map { moduleSource ->
		async {
			val jobs = moduleSource.paths.map { path ->
				async {
					processSourceFile(path)
				}
			}
			val nodes = jobs.awaitAll()
			Module(moduleSource.name, nodes)
		}
	}
	val modules = jobs.awaitAll()
	Project(projectSource.name, modules)
}

private fun processSourceFile(path: Path): SourceFileNode {
	val value = measureTimedValue {
		val source = path.readText().toCharArray()
		val path = path.absolutePath
		val lineStarts = source.getLineStarts()
		context(PzlContext(path, lineStarts)) {
			val tokens = PzlLexer.default(source).scan()
			context(PzlTokenCursor(tokens)) {
				parseSourceFileNode()
			}
		}
	}
	println("${path.absolutePath} --> ${value.duration}")
	return value.value
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