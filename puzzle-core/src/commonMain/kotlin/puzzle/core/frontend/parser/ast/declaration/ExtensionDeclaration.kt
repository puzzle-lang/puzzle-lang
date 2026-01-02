package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AnnotationCall
import puzzle.core.frontend.parser.ast.Modifier
import puzzle.core.frontend.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.parser.ast.parameter.TypeSpec
import puzzle.core.frontend.parser.ast.type.NamedType
import puzzle.core.frontend.parser.ast.type.SuperTypeReference
import puzzle.core.frontend.parser.ast.type.TypeReference

@Serializable
class ExtensionDeclaration(
	val extendedType: TypeReference,
	val modifiers: List<Modifier>,
	val superTypes: List<SuperTypeReference>,
	val withTypes: List<NamedType>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration