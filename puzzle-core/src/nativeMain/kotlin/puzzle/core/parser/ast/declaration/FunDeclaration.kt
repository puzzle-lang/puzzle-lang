package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.*
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.model.SourceLocation

@Serializable
class FunDeclaration(
	val name: FunName,
	val docComment: DocComment?,
	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val returnTypes: List<TypeReference>,
	val extension: TypeReference?,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val statements: List<Statement>,
	override val location: SourceLocation,
) : Declaration

@Serializable
sealed interface FunName

@Serializable
class IdentifierFunName(
	val name: IdentifierExpression,
) : FunName

@Serializable
class SymbolFunName(
	val kind: Symbol,
) : FunName