package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.DocComment
import puzzle.core.frontend.parser.ast.statement.Statement

@Serializable
class InitDeclaration(
	val docComment: DocComment?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Declaration