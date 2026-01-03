package puzzle.core.frontend.model

import kotlinx.serialization.Serializable

@Serializable
class AstProject(
	val name: String,
	val modules: List<AstModule>,
) {
	
	init {
		checkModules(modules)
	}
}

private fun checkModules(modules: List<AstModule>) {
	val counts = modules.groupingBy { it.name }
		.eachCount()
		.filter { it.value > 1 }
	if (counts.isEmpty()) return
	val message = counts.entries.joinToString { "模块: ${it.key} 重复了 ${it.value} 次" }
	error(message)
}