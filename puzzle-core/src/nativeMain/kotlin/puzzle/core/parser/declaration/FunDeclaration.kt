package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.binding.ContextReceiver
import puzzle.core.parser.binding.parameter.Parameter
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.statement.Statement

@Serializable
data class FunDeclaration(
	val name: String,
	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val returnTypes: List<TypeReference>,
	val extensionReceiver: ExtensionReceiver?,
	val contextReceivers: List<ContextReceiver> = emptyList(),
	val statements: List<Statement> = emptyList(),
) : Declaration

@Serializable
data class ExtensionReceiver(
	val type: TypeReference,
	val superTrait: TypeReference? = null,
)