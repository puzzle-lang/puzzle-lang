package puzzle.core.frontend.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.frontend.parser.ast.AstNode

@Serializable
sealed interface Expression : AstNode