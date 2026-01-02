package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.AnnotationCall
import puzzle.core.frontend.parser.ast.AstNode
import puzzle.core.frontend.parser.ast.Modifier
import puzzle.core.frontend.parser.ast.expression.Expression
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.parser.ast.parameter.TypeSpec
import puzzle.core.frontend.parser.ast.statement.Statement
import puzzle.core.frontend.parser.ast.type.TypeReference

@Serializable
class PropertyDeclaration(
	val name: Identifier,
	val type: TypeReference?,
	val modifiers: List<Modifier>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
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