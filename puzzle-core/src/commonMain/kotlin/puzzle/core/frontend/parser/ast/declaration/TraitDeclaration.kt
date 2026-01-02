package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AnnotationCall
import puzzle.core.frontend.parser.ast.DocComment
import puzzle.core.frontend.parser.ast.Modifier
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.parser.ast.parameter.TypeSpec
import puzzle.core.frontend.parser.ast.type.SuperTypeReference

@Serializable
class TraitDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val superTypes: List<SuperTypeReference>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration