package puzzle.core.frontend.parser.ast.statement

import kotlinx.serialization.Serializable
import puzzle.core.frontend.parser.ast.AstNode

@Serializable
sealed interface Statement : AstNode