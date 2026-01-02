package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AnnotationCall
import puzzle.core.frontend.parser.ast.DocComment
import puzzle.core.frontend.parser.ast.Modifier
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.parser.ast.parameter.Parameter
import puzzle.core.frontend.parser.ast.statement.Statement

@Serializable
class CtorDeclaration(
	val name: Identifier?,
	val docComment: DocComment?,
	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val body: List<Statement>,
	override val location: SourceLocation,
) : Declaration