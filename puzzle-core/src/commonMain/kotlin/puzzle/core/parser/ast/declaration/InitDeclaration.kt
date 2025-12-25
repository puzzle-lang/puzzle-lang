package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.DocComment
import puzzle.core.parser.ast.statement.Statement

@Serializable
class InitDeclaration(
	val docComment: DocComment?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Declaration