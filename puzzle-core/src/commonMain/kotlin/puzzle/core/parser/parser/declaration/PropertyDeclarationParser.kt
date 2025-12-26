package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.copy
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.type.NamedType
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.ast.declaration.PropertyGetter
import puzzle.core.parser.ast.declaration.PropertySetter
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionChain
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.parser.parser.statement.parseStatement
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.AccessKind.DOT
import puzzle.core.token.kinds.AccessorKind.GET
import puzzle.core.token.kinds.AccessorKind.SET
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ModifierKind.*
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON
import puzzle.core.util.isIn

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePropertyDeclaration(header: DeclarationHeader, start: SourceLocation): PropertyDeclaration {
	val (extension, name) = parseExtensionAndPropertyName()
	val type = if (cursor.match(COLON)) parseTypeReference(allowLambdaType = true) else null
	val initializer = if (cursor.match(ASSIGN)) parseExpressionChain() else null
	val isVal = VAL isIn header.modifiers
	val isLazy = LAZY isIn header.modifiers
	when {
		cursor.match(LBRACE) -> {
			if (!isVal) {
				if (isLazy) {
					syntaxError("lazy 延迟初始化必须使用 val 修饰符", header.modifiers.first { it.kind == LAZY })
				} else {
					syntaxError("计算属性必须使用 val 修饰符", cursor.previous)
				}
			}
			if (initializer != null) {
				syntaxError("计算属性不支持初始化值", cursor.previous)
			}
			val getterStart = cursor.previous.location
			val body = parseStatements()
			val end = cursor.previous.location
			return PropertyDeclaration(
				name = name,
				type = type,
				modifiers = header.modifiers,
				typeSpec = header.typeSpec,
				contextSpec = header.contextSpec,
				annotationCalls = header.annotationCalls,
				extension = extension,
				location = start span end,
				getter = PropertyGetter(
					oldValue = null,
					body = body,
					location = getterStart span end
				)
			)
		}
		
		isLazy -> syntaxError("lazy 延迟初始化属性缺少 '{'", cursor.previous.location.end)
	}
	
	var getter = parsePropertyGetter()
	val setter = parsePropertySetter()
	if (getter == null) {
		getter = parsePropertyGetter()
	}
	
	val isLate = LATE isIn header.modifiers
	if (isLate) {
		if (isVal) {
			syntaxError("late 延迟初始化属性必须使用 var 修饰符", header.modifiers.first { it.kind == VAL })
		}
		if (initializer != null) {
			syntaxError("late 延迟初始化属性不允许有初始化值", initializer)
		}
		if (getter != null) {
			syntaxError("late 延迟初始化属性不允许使用 get 属性访问器", getter)
		}
		if (setter != null) {
			syntaxError("late 延迟初始化属性不允许使用 set 属性赋值器", setter)
		}
	}
	
	if (setter == null && getter == null) {
		if (initializer == null && type == null) {
			syntaxError("属性缺少类型", name.location.end)
		}
		if (!isLate && initializer == null) {
			syntaxError("属性缺少初始化值", type!!.location.end)
		}
	}
	
	if (isVal) {
		if (setter != null) {
			syntaxError("不可变属性不允许使用 set 属性赋值器", setter)
		}
		if (initializer != null && getter != null) {
			syntaxError("不可变属性不允许同时使用初始化值和 get 属性访问器", initializer)
		}
		if (getter?.oldValue != null) {
			syntaxError("不可变属性不允许在 get 属性访问器中使用 oldValue 参数", getter.oldValue)
		}
	} else {
		if (initializer == null) {
			if (getter != null && setter == null) {
				syntaxError("属性缺少初始化值", type ?: name)
			}
			if (setter != null && getter == null) {
				syntaxError("属性缺少初始化值", type ?: name)
			}
			if (getter != null && getter.oldValue != null) {
				syntaxError("属性缺少初始化值，你在 get 属性访问器中使用了 oldValue 参数", type ?: name)
			}
			if (setter != null && setter.oldValue != null) {
				syntaxError("属性缺少初始化值，你在 set 属性赋值器中使用了 oldValue 参数", type ?: name)
			}
		} else {
			if (setter != null && getter != null && (setter.oldValue == null || getter.oldValue == null)) {
				syntaxError("在 get 属性访问器和 set 属性赋值器中未使用到 oldValue 参数，不允许有初始化值", initializer)
			}
		}
	}
	val end = cursor.previous.location
	
	return PropertyDeclaration(
		name = name,
		type = type,
		modifiers = header.modifiers,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		extension = extension,
		location = start span end,
		initializer = initializer,
		getter = getter,
		setter = setter,
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parsePropertyGetter(): PropertyGetter? {
	if (!cursor.match(GET)) return null
	val start = cursor.previous.location
	cursor.expect(LPAREN, "get 缺少 '('")
	val oldValue = if (!cursor.match(RPAREN)) {
		parseIdentifier(IdentifierTarget.GETTER_PARAMETER).also {
			cursor.expect(RPAREN, "get 缺少 ')")
		}
	} else null
	val body = when {
		cursor.match(ASSIGN) -> listOf(parseStatement())
		cursor.match(LBRACE) -> parseStatements()
		else -> syntaxError("get 缺少函数体", cursor.previous)
	}
	val end = cursor.previous.location
	return PropertyGetter(
		oldValue = oldValue,
		body = body,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parsePropertySetter(): PropertySetter? {
	if (!cursor.match(SET)) return null
	val start = cursor.previous.location
	cursor.expect(LPAREN, "set 缺少 '('")
	var newValue = parseIdentifier(IdentifierTarget.SETTER_PARAMETER)
	val oldValue = when {
		cursor.match(RPAREN) -> null
		cursor.match(COMMA) -> newValue.also {
			newValue = parseIdentifier(IdentifierTarget.SETTER_PARAMETER).also {
				cursor.expect(RPAREN, "set 缺少 ')'")
			}
		}
		
		else -> syntaxError("set 缺少 ')'", cursor.current)
	}
	val body = if (cursor.match(LBRACE)) {
		parseStatements()
	} else syntaxError("set 缺少函数体", cursor.previous)
	val end = cursor.previous.location
	return PropertySetter(
		oldValue = oldValue,
		newValue = newValue,
		body = body,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExtensionAndPropertyName(): Pair<TypeReference?, Identifier> {
	val name = parseIdentifier(IdentifierTarget.PROPERTY)
	return if (cursor.check(DOT)) {
		cursor.retreat()
		val type = parseTypeReference()
		if (type.isNullable) {
			cursor.expect(DOT, "属性缺少 '.'")
			val name = parseIdentifier(IdentifierTarget.FUN)
			type to name
		} else {
			val segments = (type.type as NamedType).segments.toMutableList()
			val name = Identifier(
				name = segments.removeLast(),
				location = type.type.location.copy(start = { it.end - 1 })
			)
			val location = type.location.copy(end = { it.end - 2 })
			val type = TypeReference(NamedType(segments, location), location)
			type to name
		}
	} else {
		null to name
	}
}