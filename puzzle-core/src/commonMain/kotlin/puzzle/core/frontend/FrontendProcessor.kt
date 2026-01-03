package puzzle.core.frontend

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.io.files.Path
import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.discovery.ProjectSourceCollector
import puzzle.core.frontend.lexer.FileLexerScanner
import puzzle.core.frontend.model.AstModule
import puzzle.core.frontend.model.AstProject
import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlParser
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.semantics.PzlSemantics
import puzzle.core.util.absolutePath
import puzzle.core.util.readText
import kotlin.time.measureTimedValue

suspend fun processFrontend(projectPath: Path): AstProject = coroutineScope {
	val projectSource = ProjectSourceCollector.collect(projectPath)
	val jobs = projectSource.modules.map { module ->
		async(Dispatchers.Default) {
			val jobs = module.paths.map { path ->
				async(Dispatchers.Default) {
					processFile(path)
				}
			}
			val nodes = jobs.awaitAll()
			AstModule(module.name, nodes)
		}
	}
	val modules = jobs.awaitAll()
	val project = AstProject(projectSource.name, modules)
	PzlSemantics.analyze(project)
	project
}

private fun processFile(path: Path): AstFile {
	val value = measureTimedValue {
		val source = path.readText().toCharArray()
		val lineStarts = source.getLineStarts()
		val context = PzlContext(path, lineStarts)
		context(context) {
			val tokens = FileLexerScanner.scan(source)
			val cursor = PzlTokenCursor(tokens)
			PzlParser.parse(cursor)
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