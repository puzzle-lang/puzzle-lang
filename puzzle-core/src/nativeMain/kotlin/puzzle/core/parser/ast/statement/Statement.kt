package puzzle.core.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode

@Serializable
sealed interface Statement : AstNode