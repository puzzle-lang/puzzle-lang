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
		serializersModule = AstSerializersModule
	}
	
	fun write(projectPath: Path, project: AstProject) {
		val buildPath = Path(projectPath, "build", "ast")
		if (buildPath.exists()) {
			buildPath.delete()
		}
		project.modules.forEach { module ->
			module.nodes.forEach { node ->
				if (node.path == null && !node.isBuiltin) return@forEach
				val astPath = if (node.isBuiltin) {
					getBuiltinPath(buildPath, module.name, node.name)
				} else {
					getAstPath(projectPath, buildPath, node.path!!)
				}
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
	
	private fun getBuiltinPath(buildPath: Path, moduleName: String, nodeName: String): Path {
		val nodeName = nodeName.removeSuffix(".pzl")
		return Path(buildPath, moduleName, "src", "main", "puzzle", "$nodeName.json")
	}
	
	private fun getAstPath(projectPath: Path, buildPath: Path, sourcePath: Path): Path {
		val path = sourcePath.absolutePath.removePrefix(projectPath.absolutePath).removeSuffix(".pzl")
		return Path(buildPath, "$path.json")
	}
}