package puzzle.core.frontend.model

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AstFile

@Serializable
class AstModule(
	val name: String,
	val nodes: List<AstFile>,
)