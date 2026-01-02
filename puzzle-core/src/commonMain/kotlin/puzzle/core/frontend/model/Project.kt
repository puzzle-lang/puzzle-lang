package puzzle.core.frontend.model

import kotlinx.serialization.Serializable

@Serializable
class Project(
	val name: String,
	val modules: List<Module>,
)