package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.node.AstNode

@Serializable
sealed interface Declaration : AstNode