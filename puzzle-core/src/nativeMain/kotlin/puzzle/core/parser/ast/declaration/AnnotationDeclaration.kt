package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.symbol.Modifier

@Serializable
class AnnotationDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val typeSpec: TypeSpec?,
	val annotationCalls: List<AnnotationCall>
) : Declaration