package puzzle.core.frontend.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.*
import puzzle.core.frontend.parser.ast.expression.Identifier
import puzzle.core.frontend.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.parser.ast.parameter.Parameter
import puzzle.core.frontend.parser.ast.parameter.TypeSpec
import puzzle.core.frontend.parser.ast.statement.Statement
import puzzle.core.frontend.parser.ast.type.TypeReference

@Serializable
class FunDeclaration(
	val name: FunName,
	val docComment: DocComment?,
	val parameters: List<Parameter>,
	val modifiers: List<Modifier>,
	val returnSpec: ReturnSpec?,
	val extension: TypeReference?,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val body: List<Statement>,
	override val location: SourceLocation,
) : TopLevelAllowedDeclaration

@Serializable
sealed interface ReturnSpec : AstNode

@Serializable
class SingleReturnSpec(
	val type: TypeReference,
	override val location: SourceLocation = type.location,
) : ReturnSpec

@Serializable
class MultiReturnSpec(
	val types: List<TypeReference>,
	override val location: SourceLocation,
) : ReturnSpec

@Serializable
sealed interface FunName : AstNode

@Serializable
class IdentifierFunName(
	val name: Identifier,
	override val location: SourceLocation = name.location,
) : FunName

@Serializable
sealed interface OperatorFunName : FunName

@Serializable
class SymbolFunName(
	val symbol: Symbol,
	override val location: SourceLocation = symbol.location,
) : OperatorFunName

@Serializable
class IndexAccessFunName(
	val kind: IndexAccessKind,
	override val location: SourceLocation,
) : OperatorFunName

@Serializable
enum class IndexAccessKind {
	GETTER,
	SETTER
}