package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.DocComment
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.model.SourceLocation

@Serializable
class EnumDeclaration(
	val name: Identifier,
	val docComment: DocComment?,
	val modifiers: List<Modifier>,
	val parameters: List<Parameter>,
	val entries: List<EnumEntry>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val members: List<Declaration>,
	override val location: SourceLocation,
) : Declaration

@Serializable
class EnumEntry(
	val name: Identifier,
	val members: List<Declaration>,
	override val location: SourceLocation,
) : AstNode