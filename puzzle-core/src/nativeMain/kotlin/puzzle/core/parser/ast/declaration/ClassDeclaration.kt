package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.token.ModifierKind

@Serializable
class ClassDeclaration(
	val name: String,
	val modifiers: List<ModifierKind>,
	val constructorModifiers: List<ModifierKind>,
	val parameters: List<Parameter>,
	val superTypes: List<SuperType>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<Declaration>,
) : Declaration