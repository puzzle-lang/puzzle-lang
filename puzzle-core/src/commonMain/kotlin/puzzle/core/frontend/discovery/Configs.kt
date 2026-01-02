package puzzle.core.frontend.discovery

import kotlinx.serialization.Serializable

@Serializable
class ProjectConfig(
	val name: String = "",
	val version: String = "",
	val modules: List<String> = emptyList(),
)

@Serializable
class ModuleConfig(
	val name: String = "",
	val version: String = "",
	val group: String = "",
	val deps: List<String> = emptyList(),
)