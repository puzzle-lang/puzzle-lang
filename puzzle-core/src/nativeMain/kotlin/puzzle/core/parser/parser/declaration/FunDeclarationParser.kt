package puzzle.core.parser.parser.declaration

import puzzle.core.constants.PzlTypes
import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.declaration.FunDeclaration
import puzzle.core.parser.ast.declaration.FunName
import puzzle.core.parser.ast.declaration.IdentifierFunName
import puzzle.core.parser.ast.declaration.SymbolFunName
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.tryParseIdentifierName
import puzzle.core.parser.parser.parameter.parameter.parseFunParameters
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.*
import puzzle.core.token.AssignmentKind.*
import puzzle.core.token.OperatorKind.*
import puzzle.core.token.SymbolKind.INDEX_GET
import puzzle.core.token.SymbolKind.INDEX_SET

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseFunDeclaration(
	typeSpec: TypeSpec?,
	contextSpec: ContextSpec?,
	modifiers: List<ModifierKind>,
	annotationCalls: List<AnnotationCall>,
): FunDeclaration {
	val (extension, funName) = parseExtensionAndFunName()
	val parameters = parseFunParameters()
	val returnTypes = mutableListOf<TypeReference>()
	if (cursor.match(SymbolKind.COLON)) {
		do {
			returnTypes += parseTypeReference(isSupportedLambdaType = true)
		} while (cursor.match(SeparatorKind.COMMA))
	} else {
		returnTypes += TypeReference(PzlTypes.Unit)
	}
	val expressions = if (cursor.match(BracketKind.Start.LBRACE)) parseStatements() else emptyList()
	return FunDeclaration(
		name = funName,
		parameters = parameters,
		modifiers = modifiers,
		returnTypes = returnTypes,
		extension = extension,
		typeSpec = typeSpec,
		contextSpec = contextSpec,
		annotationCalls = annotationCalls,
		statements = expressions
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExtensionAndFunName(): Pair<TypeReference?, FunName> {
	val name = tryParseIdentifierName(IdentifierNameTarget.FUN)
		?: return null to (tryParseOperatorFunName() ?: syntaxError("函数缺少名称", cursor.current))
	if (!cursor.check(AccessKind.DOT)) {
		return null to IdentifierFunName(name)
	}
	cursor.retreat()
	val segments = mutableListOf<String>()
	do {
		val type = if (cursor.match(AccessKind.QUESTION_DOT)) {
			TypeReference(NamedType(segments), isNullable = true)
		} else null
		val segment = tryParseIdentifierName(IdentifierNameTarget.TYPE_REFERENCE)
			?: tryParseOperatorFunName()
			?: syntaxError("无法识别标识符", cursor.current)
		if (type != null) {
			val funName = if (segment is String) IdentifierFunName(segment) else segment as SymbolFunName
			return type to funName
		}
		if (segment is SymbolFunName) {
			val type = TypeReference(NamedType(segments))
			return type to segment
		}
		segments += segment as String
	} while (cursor.match(AccessKind.DOT))
	val funName = IdentifierFunName(segments.removeLast())
	val type = TypeReference(NamedType(segments))
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
	var operator = overloadableOperators.find { cursor.match(it) }
	if (operator != null) {
		return SymbolFunName(operator)
	}
	operator = notOverloadableOperators.find { cursor.match(it) }
	if (operator != null) {
		syntaxError("'${operator.value}' 运算符不支持被重载", cursor.previous)
	}
	return null
}