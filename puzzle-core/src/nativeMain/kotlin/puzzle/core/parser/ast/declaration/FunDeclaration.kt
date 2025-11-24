package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.ContextReceiver
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.parser.ast.node.TypeReference
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.symbol.Modifier

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