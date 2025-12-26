package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.DocComment
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.TypeSpec

@Serializable
class StructDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val primaryAnnotationCalls: List<AnnotationCall>,
	val primaryCtorModifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val superTypeReferences: List<SuperTypeReference>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val inits: List<InitDeclaration>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration
