package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.symbol.Modifier

@Serializable
class PropertyDeclaration(
    val name: String,
    val type: TypeReference?,
    val modifiers: List<Modifier>,
    val extension: TypeReference?,
    val typeSpec: TypeSpec?,
    val contextSpec: ContextSpec?,
    val initializer: Expression?
) : Declaration