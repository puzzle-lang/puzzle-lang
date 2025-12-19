package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.Symbol
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.declaration.FunDeclaration
import puzzle.core.parser.ast.declaration.FunName
import puzzle.core.parser.ast.declaration.IdentifierFunName
import puzzle.core.parser.ast.declaration.SymbolFunName
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.tryParseIdentifier
import puzzle.core.parser.parser.expression.tryParseIdentifierString
import puzzle.core.parser.parser.parameter.parameter.parseFunParameters
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.AccessKind.DOT
import puzzle.core.token.kinds.AccessKind.QUESTION_DOT
import puzzle.core.token.kinds.AssignmentKind.*
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.IndexKind.INDEX_GET
import puzzle.core.token.kinds.IndexKind.INDEX_SET
import puzzle.core.token.kinds.OperatorKind.*
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseFunDeclaration(header: DeclarationHeader, start: SourceLocation): FunDeclaration {
	val (extension, funName) = parseExtensionAndFunName()
	val parameters = parseFunParameters()
	val returnTypes = mutableListOf<TypeReference>()
	if (cursor.match(COLON)) {
		do {
			returnTypes += parseTypeReference(isSupportedLambdaType = true)
		} while (cursor.match(COMMA))
	}
	val expressions = if (cursor.match(LBRACE)) parseStatements() else emptyList()
	val end = cursor.previous.location
	return FunDeclaration(
		name = funName,
		docComment = header.docComment,
		parameters = parameters,
		modifiers = header.modifiers,
		returnTypes = returnTypes,
		extension = extension,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		body = expressions,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExtensionAndFunName(): Pair<TypeReference?, FunName> {
	val name = tryParseIdentifier(IdentifierTarget.FUN)
		?: return null to (tryParseOperatorFunName() ?: syntaxError("函数缺少名称", cursor.current))
	val start = name.location
	if (!cursor.check(DOT)) {
		return null to IdentifierFunName(name)
	}
	cursor.retreat()
	val segments = mutableListOf<String>()
	do {
		val type = if (cursor.match(QUESTION_DOT)) {
			TypeReference(
				type = NamedType(segments, start span cursor.offset(-2).location),
				location = start span cursor.previous.location,
				isNullable = true
			)
		} else null
		val name = tryParseIdentifierString(IdentifierTarget.TYPE_REFERENCE)
			?: tryParseOperatorFunName()
			?: syntaxError("无法识别标识符", cursor.current)
		if (type != null) {
			return type to when (name) {
				is String -> IdentifierFunName(Identifier(name, cursor.previous.location))
				else -> name as SymbolFunName
			}
		}
		if (name is SymbolFunName) {
			val location = start span cursor.previous.location
			val type = TypeReference(NamedType(segments, location), location)
			return type to name
		}
		segments += name as String
	} while (cursor.match(DOT))
	val funName = IdentifierFunName(Identifier(segments.removeLast(), cursor.previous.location))
	val location = start span cursor.previous.location
	val type = TypeReference(NamedType(segments, location), location)
	return type to funName
}

private val overloadableOperators = arrayOf<SymbolKind>(
	PLUS, MINUS, NOT, BIT_NOT, DOUBLE_PLUS, DOUBLE_MINUS,
	STAR, SLASH, PERCENT, DOUBLE_STAR,
	EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS,
	CONTAINS, NOT_CONTAINS,
	BIT_AND, BIT_OR, BIT_XOR, SHL, SHR, USHR,
	PLUS_ASSIGN, MINUS_ASSIGN, STAR_ASSIGN, SLASH_ASSIGN, PERCENT_ASSIGN,
	INDEX_GET, INDEX_SET, RANGE_TO, RANGE_UNTIL
)

private val notOverloadableOperators = arrayOf<SymbolKind>(
	TRIPLE_EQUALS, TRIPLE_NOT_EQUALS,
	AND, OR
)

context(_: PzlContext, cursor: PzlTokenCursor)
private fun tryParseOperatorFunName(): SymbolFunName? {
	var kind = overloadableOperators.find { cursor.match(it) }
	if (kind != null) {
		return SymbolFunName(Symbol(kind, cursor.previous.location))
	}
	kind = notOverloadableOperators.find { cursor.match(it) }
	if (kind != null) {
		syntaxError("'${kind.value}' 运算符不支持被重载", cursor.previous)
	}
	return null
}