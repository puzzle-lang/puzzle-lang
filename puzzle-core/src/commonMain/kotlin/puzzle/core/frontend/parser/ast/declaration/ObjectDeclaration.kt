package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AnnotationCall
import puzzle.core.frontend.parser.ast.DocComment
import puzzle.core.frontend.parser.ast.Modifier
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.parser.ast.parameter.Parameter
import puzzle.core.frontend.parser.ast.type.NamedType
import puzzle.core.frontend.parser.ast.type.SuperType

@Serializable
class ObjectDeclaration(
	val name: Identifier?,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val primaryAnnotationCalls: List<AnnotationCall>,
	val primaryCtorModifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val superTypes: List<SuperType>,
	val withTypes: List<NamedType>,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val inits: List<InitDeclaration>,
	val ctors: List<CtorDeclaration>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration