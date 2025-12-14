package puzzle.core.parser.ast.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.Parameter
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.token.ModifierKind
import puzzle.core.token.SymbolKind
import puzzle.core.util.FunNameSerializer
import puzzle.core.util.ModifierKindListSerializer
import puzzle.core.util.SymbolKindSerializer

@Serializable
class FunDeclaration(
	@Serializable(with = FunNameSerializer::class)
	val name: FunName,
	val parameters: List<Parameter>,
	@Serializable(with = ModifierKindListSerializer::class)
	val modifiers: List<ModifierKind>,
	val returnTypes: List<TypeReference>,
	val extension: TypeReference?,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val annotationCalls: List<AnnotationCall>,
	val statements: List<Statement>,
) : Declaration

@Serializable
sealed interface FunName

@Serializable
class IdentifierFunName(
	val name: String
) : FunName

@Serializable
class SymbolFunName(
	@Serializable(with = SymbolKindSerializer::class)
	val symbol: SymbolKind
) : FunName