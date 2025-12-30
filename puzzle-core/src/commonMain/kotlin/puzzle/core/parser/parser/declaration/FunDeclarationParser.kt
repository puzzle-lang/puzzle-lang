package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.copy
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Symbol
import puzzle.core.parser.ast.declaration.*
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.ast.type.LambdaType
import puzzle.core.parser.ast.type.NamedType
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.type.copy
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.tryParseIdentifier
import puzzle.core.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.parser.parser.parameter.parameter.parseParameters
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.AccessKind.DOT
import puzzle.core.token.kinds.AccessKind.QUESTION_DOT
import puzzle.core.token.kinds.AssignmentKind.*
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.token.kinds.OperatorKind.*
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind
import puzzle.core.token.kinds.SymbolKind.COLON
import puzzle.core.token.kinds.SymbolKind.QUESTION

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseFunDeclaration(header: DeclarationHeader, start: SourceLocation): FunDeclaration {
	val (extension, funName) = parseExtensionAndFunName()
	val parameters = parseParameters(ParameterTarget.FUN)
	val returnSpec = when {
		!cursor.match(COLON) -> null
		cursor.match(LBRACKET) -> {
			val start = cursor.previous.location
			val types = buildList {
				while (!cursor.match(RBRACKET)) {
					this += parseTypeReference(allowLambda = true)
					if (!cursor.check(RBRACKET)) {
						cursor.expect(COMMA, "多返回值类型列表缺少 ','")
					}
				}
			}
			if (types.isEmpty()) {
				syntaxError("多返回值缺少类型", cursor.previous)
			}
			if (types.size == 1) {
				syntaxError("多返回值至少需要2个类型", cursor.previous)
			}
			if (cursor.match(QUESTION)) {
				syntaxError("语法错误", cursor.previous)
			}
			val end = cursor.previous.location
			MultiReturnSpec(types, start span end)
		}
		
		else -> {
			val type = parseTypeReference(allowLambda = true)
			SingleReturnSpec(type)
		}
	}
	val expressions = if (cursor.match(LBRACE)) parseStatements() else emptyList()
	val end = cursor.previous.location
	return FunDeclaration(
		name = funName,
		docComment = header.docComment,
		parameters = parameters,
		modifiers = header.modifiers,
		returnSpec = returnSpec,
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
	val name = tryParseIdentifier(IdentifierTarget.FUN) ?: run {
		val funName = tryParseOperatorFunName()
			?: syntaxError("函数缺少名称", cursor.current)
		return null to funName
	}
	if (!cursor.check { it.kind == DOT || it.kind == QUESTION_DOT || it.kind == LT }) {
		return null to IdentifierFunName(name)
	}
	cursor.retreat()
	var extension = parseTypeReference(allowLambda = true)
	val type = extension.type
	if (type is LambdaType || (type is NamedType && type.typeArguments.isNotEmpty())) {
		extension = when {
			cursor.match(DOT) -> extension
			cursor.match(QUESTION_DOT) -> extension.copy(
				isNullable = true,
				location = cursor.previous.location.copy(end = { it - 1 })
			)
			
			else -> syntaxError("扩展函数缺少 '.'", cursor.current)
		}
		val funName = tryParseIdentifier(IdentifierTarget.FUN)?.let { IdentifierFunName(it) }
			?: tryParseOperatorFunName()
			?: syntaxError("函数缺少名称", cursor.current)
		return extension to funName
	}
	type as NamedType
	if (cursor.match { it.kind == DOT || it.kind == QUESTION_DOT }) {
		extension = extension.copy(
			isNullable = true,
			location = cursor.previous.location.copy(end = { it - 1 })
		)
		val funName = tryParseIdentifier(IdentifierTarget.FUN)?.let { IdentifierFunName(it) }
			?: tryParseOperatorFunName()
			?: syntaxError("函数缺少名称", cursor.current)
		return extension to funName
	}
	val segments = type.segments.toMutableList()
	val segment = segments.removeLast()
	extension = extension.copy(
		type = NamedType(segments, extension.location span cursor.offset(-3).location)
	)
	val funName = IdentifierFunName(Identifier(segment, cursor.previous.location))
	return extension to funName
}

private val overloadableSymbols = setOf(
	PLUS, MINUS, NOT, BIT_NOT, DOUBLE_PLUS, DOUBLE_MINUS,
	STAR, SLASH, PERCENT, DOUBLE_STAR,
	EQUALS, NOT_EQUALS, GT, GT_EQUALS, LT, LT_EQUALS,
	BIT_AND, BIT_OR, BIT_XOR, SHL, SHR, USHR,
	PLUS_ASSIGN, MINUS_ASSIGN, STAR_ASSIGN, SLASH_ASSIGN, PERCENT_ASSIGN,
	RANGE_TO, RANGE_UNTIL
)

private val notOverloadableSymbols = setOf(
	TRIPLE_EQUALS, TRIPLE_NOT_EQUALS,
	AND, OR
)

context(_: PzlContext, cursor: PzlTokenCursor)
private fun tryParseOperatorFunName(): FunName? {
	if (cursor.match { it.kind in overloadableSymbols }) {
		val token = cursor.previous
		return SymbolFunName(Symbol(token.kind as SymbolKind, token.location))
	}
	if (cursor.match(LBRACKET, RBRACKET)) {
		return if (cursor.match(ASSIGN)) {
			val location = cursor.offset(-3).location span cursor.previous.location
			IndexAccessFunName(IndexAccessKind.SETTER, location)
		} else {
			val location = cursor.offset(-2).location span cursor.previous.location
			IndexAccessFunName(IndexAccessKind.GETTER, location)
		}
	}
	if (cursor.match { it.kind in notOverloadableSymbols }) {
		val token = cursor.previous
		syntaxError("'${token.value}' 运算符不支持被重载", cursor.previous)
	}
	return null
}