package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.expression.Expression

@Serializable
class VariableDeclaration(
    val name: String,
    val type: TypeReference?,
    val isMutable: Boolean,
    val initializer: Expression?,
) : Declaration