package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.Modifier
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.parameter.Parameter
import puzzle.core.parser.statement.Statement

@Serializable
data class FunDeclaration(
	val name: String,
	val parameters: List<Parameter>,
	val modifiers: Set<Modifier>,
	val returnTypes: List<TypeReference>,
	val statements: List<Statement> = emptyList(),
) : Declaration