package puzzle.core.parser.ast.expression

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode

@Serializable
sealed interface Expression : AstNode