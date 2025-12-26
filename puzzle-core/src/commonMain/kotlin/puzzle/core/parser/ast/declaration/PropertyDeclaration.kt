package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.AstNode
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.statement.Statement

@Serializable
class PropertyDeclaration(
	val name: Identifier,
	val type: TypeReference?,
	val modifiers: List<Modifier>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val extension: TypeReference?,
	override val location: SourceLocation,
	val initializer: Expression? = null,
	val getter: PropertyGetter? = null,
	val setter: PropertySetter? = null,
) : TopLevelAllowedDeclaration

@Serializable
class PropertyGetter(
	val oldValue: Identifier?,
	val body: List<Statement>,
	override val location: SourceLocation,
) : AstNode

@Serializable
class PropertySetter(
	val oldValue: Identifier?,
	val newValue: Identifier,
	val body: List<Statement>,
	override val location: SourceLocation,
) : AstNode