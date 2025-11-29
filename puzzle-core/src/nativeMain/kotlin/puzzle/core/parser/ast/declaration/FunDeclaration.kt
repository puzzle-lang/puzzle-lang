package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.symbol.Modifier

@Serializable
class FunDeclaration(
	val name: String,
	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val returnTypes: List<TypeReference>,
	val extension: TypeReference?,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val statements: List<Statement> = emptyList(),
) : Declaration