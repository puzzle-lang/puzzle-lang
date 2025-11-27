package puzzle.core.parser.parser.expression

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.*
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget

class PostfixExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<PostfixExpressionParser>(::PostfixExpressionParser)
	
	context(_: PzlContext)
	fun parse(left: Expression?): Expression {
		var receiver = parseInitialExpression(left)
		receiver = parseExpression(receiver)
		while (isAccessOperator(cursor)) {
			val operator = cursor.previous.type.toAccessOperator()
			var expression = parseIdentifierExpression()
			expression = parseExpression(expression)
			receiver = PropertyAccessExpression(
				receiver = receiver,
				operator = operator,
				expression = expression
			)
		}
		return receiver
	}
	
	private fun isInvoke(): Boolean {
		return cursor.match(PzlTokenType.LPAREN) || cursor.match(PzlTokenType.LBRACKET)
	}
	
	context(_: PzlContext)
	private fun parseInvokeExpression(callee: Expression): InvokeExpression {
		return when (cursor.previous.type) {
			InvokeType.CALL.startTokenType -> {
				val arguments = parseArguments(cursor, InvokeType.CALL)
				CallExpression(callee, arguments)
			}
			
			InvokeType.INDEX_ACCESS.startTokenType -> {
				val arguments =
					parseArguments(cursor, InvokeType.INDEX_ACCESS)
				if (arguments.isEmpty()) {
					syntaxError("索引访问不允许空参数", cursor.previous)
				}
				IndexAccessExpression(callee, arguments)
			}
			
			else -> error("不支持的调用符号")
		}
	}
	
	context(_: PzlContext)
	private fun parseIdentifierExpression(): Expression {
		val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.ACCESS_OPERATOR)
		return IdentifierExpression(name)
	}
	
	context(_: PzlContext)
	private fun parseInitialExpression(receiver: Expression?): Expression {
		val token = cursor.previous
		return when (token.type) {
			PzlTokenType.NUMBER -> NumberLiteral(token.value)
			PzlTokenType.STRING -> StringLiteral(token.value)
			PzlTokenType.CHAR -> CharLiteral(token.value)
			PzlTokenType.TRUE -> BooleanLiteral(true)
			PzlTokenType.FALSE -> BooleanLiteral(false)
			PzlTokenType.THIS -> ThisLiteral
			PzlTokenType.SUPER -> SuperLiteral
			PzlTokenType.NULL -> NullLiteral
			in accessTokenTypes -> {
				cursor.retreat()
				when {
					receiver != null -> {
						if (receiver is PrefixUnaryExpression) {
							syntaxError("访问操作符前不可以使用前缀一元运算符", cursor.offset(offset = -2))
						} else receiver
					}
					
					token.type == PzlTokenType.DOUBLE_COLON -> ThisLiteral
					else -> syntaxError("'.' 和 '?.' 访问操作符前缺少接收者", cursor.previous)
				}
			}
			
			else -> if (IdentifierNameParser.check(token)) {
				IdentifierExpression(token.value)
			} else {
				syntaxError("不支持的基础表达式", token)
			}
		}
	}
	
	context(_: PzlContext)
	private fun parseExpression(receiver: Expression): Expression {
		var receiver = receiver
		while (cursor.match(PzlTokenType.BANG)) {
			if (receiver !is NonNullAssertionExpression) {
				receiver = NonNullAssertionExpression(receiver)
			}
		}
		while (isInvoke()) {
			receiver = parseInvokeExpression(receiver)
		}
		while (cursor.match(PzlTokenType.BANG)) {
			if (receiver !is NonNullAssertionExpression) {
				receiver = NonNullAssertionExpression(receiver)
			}
		}
		return receiver
	}
}

private val accessTokenTypes = listOf(
	PzlTokenType.DOT,
	PzlTokenType.QUESTION_DOT,
	PzlTokenType.DOUBLE_COLON
)

fun isAccessOperator(cursor: PzlTokenCursor): Boolean {
	return accessTokenTypes.any { cursor.match(it) }
}