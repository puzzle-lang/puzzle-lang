package puzzle.core.parser.parser.declaration

import puzzle.core.constants.PzlTypes
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.declaration.FunDeclaration
import puzzle.core.parser.ast.declaration.FunName
import puzzle.core.parser.ast.declaration.IdentifierFunName
import puzzle.core.parser.ast.declaration.OperatorFunName
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.matcher.statement.parseStatement
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.tryParseIdentifierName
import puzzle.core.parser.parser.parameter.parameter.parseFunParameters
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseFunDeclaration(
	typeSpec: TypeSpec?,
	contextSpec: ContextSpec?,
	modifiers: List<Modifier>,
	annotationCalls: List<AnnotationCall>,
): FunDeclaration {
	val (extension, funName) = parseExtensionAndFunName()
	val parameters = parseFunParameters()
	val returnTypes = mutableListOf<TypeReference>()
	if (cursor.match(COLON)) {
		do {
			returnTypes += parseTypeReference(isSupportedLambdaType = true)
		} while (cursor.match(COMMA))
	} else {
		returnTypes += TypeReference(PzlTypes.Unit)
	}
	val statements = if (cursor.match(LBRACE)) {
		buildList {
			while (!cursor.match(RBRACE)) {
				this += parseStatement()
			}
		}
	} else emptyList()
	return FunDeclaration(
		name = funName,
		parameters = parameters,
		modifiers = modifiers,
		returnTypes = returnTypes,
		extension = extension,
		typeSpec = typeSpec,
		contextSpec = contextSpec,
		annotationCalls = annotationCalls,
		statements = statements
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExtensionAndFunName(): Pair<TypeReference?, FunName> {
	val name = tryParseIdentifierName(IdentifierNameTarget.FUN)
		?: return null to (tryParseOperatorFunName() ?: syntaxError("函数缺少名称", cursor.current))
	if (!cursor.check(DOT)) {
		return null to IdentifierFunName(name)
	}
	cursor.retreat()
	val segments = mutableListOf<String>()
	do {
		val type = if (cursor.match(QUESTION_DOT)) {
			TypeReference(NamedType(segments), isNullable = true)
		} else null
		val segment = tryParseIdentifierName(IdentifierNameTarget.TYPE_REFERENCE)
			?: tryParseOperatorFunName()
			?: syntaxError("无法识别标识符", cursor.current)
		if (type != null) {
			val funName = if (segment is String) IdentifierFunName(segment) else segment as OperatorFunName
			return type to funName
		}
		if (segment is OperatorFunName) {
			val type = TypeReference(NamedType(segments))
			return type to segment
		}
		segments += segment as String
	} while (cursor.match(DOT))
	val funName = IdentifierFunName(segments.removeLast())
	val type = TypeReference(NamedType(segments))
	return type to funName
}

private val overloadableOperators = arrayOf(
	PLUS, MINUS, NOT, BIT_NOT, DOUBLE_PLUS, DOUBLE_MINUS,
	STAR, SLASH, PERCENT, DOUBLE_STAR,
	EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS,
	CONTAINS, NOT_CONTAINS,
	BIT_AND, BIT_OR, BIT_XOR, SHL, SHR, USHR,
	PLUS_ASSIGN, MINUS_ASSIGN, STAR_ASSIGN, SLASH_ASSIGN, PERCENT_ASSIGN,
	INDEX_GET, INDEX_SET, RANGE_TO, RANGE_UNTIL
)

private val notOverloadableOperators = arrayOf(
	TRIPLE_EQUALS, TRIPLE_NOT_EQUALS,
	AND, OR
)

context(_: PzlContext, cursor: PzlTokenCursor)
private fun tryParseOperatorFunName(): OperatorFunName? {
	var operator = overloadableOperators.find { cursor.match(it) }
	if (operator != null) {
		return OperatorFunName(operator.value)
	}
	operator = notOverloadableOperators.find { cursor.match(it) }
	if (operator != null) {
		syntaxError("'${operator.value}' 运算符不支持被重载", cursor.previous)
	}
	return null
}