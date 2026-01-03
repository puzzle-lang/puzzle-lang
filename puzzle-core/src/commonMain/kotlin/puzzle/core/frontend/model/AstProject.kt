package puzzle.core.frontend.model

import kotlinx.serialization.Serializable

@Serializable
class AstProject(
	val name: String,
	val modules: List<AstModule>,
)