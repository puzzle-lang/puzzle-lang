package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.symbol.Modifier

@Serializable
class UniqueDeclaration(
    val name: String,
    val modifiers: List<Modifier>,
    val contextSpec: ContextSpec?,
    val members: List<Declaration>
) : Declaration