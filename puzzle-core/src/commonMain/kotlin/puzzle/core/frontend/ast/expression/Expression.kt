package puzzle.core.frontend.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AstNode

@Serializable
sealed interface Expression : AstNode