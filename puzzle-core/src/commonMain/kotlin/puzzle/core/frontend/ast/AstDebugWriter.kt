package puzzle.core.frontend.ast

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.writeString
import kotlinx.serialization.json.Json
import puzzle.core.frontend.model.AstProject
import puzzle.core.util.*

object AstDebugWriter {
	
	private val json = Json {
		prettyPrint = true
		encodeDefaults = true
		classDiscriminator = "class"
		ignoreUnknownKeys = true
		serializersModule = astSerializersModule
	}
	
	fun write(projectPath: Path, project: AstProject) {
		val buildPath = Path(projectPath, "build", "ast")
		if (buildPath.exists()) {
			buildPath.delete()
		}
		project.modules.forEach { module ->
			module.nodes.forEach { node ->
				if (node.isBuiltin || node.path == null) return@forEach
				val astPath = getAstPath(projectPath, buildPath, node.path)
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
	
	private fun getAstPath(projectPath: Path, buildPath: Path, sourcePath: Path): Path {
		val path = sourcePath.absolutePath.removePrefix(projectPath.absolutePath).removeSuffix(".pzl")
		return Path(buildPath, "$path.json")
	}
}