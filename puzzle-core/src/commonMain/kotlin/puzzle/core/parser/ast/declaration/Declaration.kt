package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AstNode

@Serializable
sealed interface Declaration : AstNode

@Serializable
sealed interface TopLevelAllowedDeclaration : Declaration