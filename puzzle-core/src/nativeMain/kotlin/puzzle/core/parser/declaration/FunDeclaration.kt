package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.binding.Parameter
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.statement.Statement

@Serializable
data class FunDeclaration(
	val name: String,
	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val returnTypes: List<TypeReference>,
	val extensionReceiver: TypeReference? = null,
	val implementedTrait: TypeReference? = null,
	val contextReceivers: List<Parameter> = emptyList(),
	val statements: List<Statement> = emptyList(),
) : Declaration