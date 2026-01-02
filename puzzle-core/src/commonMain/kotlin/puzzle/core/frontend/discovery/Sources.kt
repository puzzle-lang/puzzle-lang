package puzzle.core.frontend.discovery

import kotlinx.io.files.Path

class ProjectSource(
	val name: String,
	val modules: List<ModuleSource>,
)

class ModuleSource(
	val name: String,
	val paths: List<Path>,
)