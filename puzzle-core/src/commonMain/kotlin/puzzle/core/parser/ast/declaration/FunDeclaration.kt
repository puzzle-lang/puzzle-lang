package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.*
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.parameter.DeclarationContextSpec
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.ast.type.TypeReference

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
sealed interface FunName

@Serializable
class IdentifierFunName(
	val name: Identifier,
) : FunName

@Serializable
class SymbolFunName(
	val symbol: Symbol,
) : FunName

@Serializable
class IndexAccessFunName(
	val indexAccess: IndexAccess,
) : FunName

@Serializable
class IndexAccess(
	val kind: IndexAccessKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
enum class IndexAccessKind {
	GETTER,
	SETTER
}