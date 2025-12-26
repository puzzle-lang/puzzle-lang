package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec

@Serializable
class ExtensionDeclaration(
	val extendedType: TypeReference,
	val modifiers: List<Modifier>,
	val superTraits: List<SuperTypeReference>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<TopLevelAllowedDeclaration>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration