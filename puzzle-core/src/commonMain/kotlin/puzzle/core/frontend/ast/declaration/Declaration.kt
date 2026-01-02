package puzzle.core.frontend.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.AstNode

@Serializable
sealed interface Declaration : AstNode

@Serializable
sealed interface TopLevelAllowedDeclaration : Declaration