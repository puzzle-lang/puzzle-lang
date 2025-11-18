package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.*

class PostfixExpressionParser(
	private val ctx: PzlParserContext
) {
	context(_: PzlContext)
	fun parse(left: Expression?): Expression {
		var receiver = parseInitialExpression(left)
		receiver = parseExpression(receiver)
		while (isAccessOperator(ctx)) {
			val operator = ctx.previous.type.toAccessOperator()
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
		return ctx.match(PzlTokenType.LPAREN) || ctx.match(PzlTokenType.LBRACKET)
	}
	
	context(_: PzlContext)
	private fun parseInvokeExpression(callee: Expression): InvokeExpression {
		return when (ctx.previous.type) {
			InvokeType.CALL.startTokenType -> {
				val arguments = parseArguments(ctx, InvokeType.CALL)
				CallExpression(callee, arguments)
			}
			
			InvokeType.INDEX_ACCESS.startTokenType -> {
				val arguments =
					parseArguments(ctx, InvokeType.INDEX_ACCESS)
				if (arguments.isEmpty()) {
					syntaxError("索引访问不允许空参数", ctx.previous)
				}
				IndexAccessExpression(callee, arguments)
			}
			
			else -> error("不支持的调用符号")
		}
	}
	
	context(_: PzlContext)
	private fun parseIdentifierExpression(): Expression {
		ctx.expect(PzlTokenType.IDENTIFIER, "访问操作符后必须跟标识符")
		return IdentifierExpression(ctx.previous.value)
	}
	
	context(_: PzlContext)
	private fun parseInitialExpression(receiver: Expression?): Expression {
		val token = ctx.previous
		return when (token.type) {
			PzlTokenType.NUMBER -> NumberLiteral(token.value)
			PzlTokenType.STRING -> StringLiteral(token.value)
			PzlTokenType.CHAR -> CharLiteral(token.value)
			PzlTokenType.TRUE -> BooleanLiteral(true)
			PzlTokenType.FALSE -> BooleanLiteral(false)
			PzlTokenType.THIS -> ThisLiteral
			PzlTokenType.SUPER -> SuperLiteral
			PzlTokenType.NULL -> NullLiteral
			PzlTokenType.IDENTIFIER -> IdentifierExpression(token.value)
			in accessTokenTypes -> {
				ctx.retreat()
				when {
					receiver != null -> {
						if (receiver is PrefixUnaryExpression) {
							syntaxError("访问操作符前不可以使用前缀一元运算符", ctx.peek(offset = -2)!!)
						} else receiver
					}
					
					token.type == PzlTokenType.DOUBLE_COLON -> ThisLiteral
					else -> syntaxError("'.' 和 '?.' 访问操作符前缺少接收者", ctx.previous)
				}
			}
			
			else -> syntaxError("不支持的基础表达式", token)
		}
	}
	
	context(_: PzlContext)
	private fun parseExpression(receiver: Expression): Expression {
		var receiver = receiver
		while (ctx.match(PzlTokenType.BANG)) {
			if (receiver !is NonNullAssertionExpression) {
				receiver = NonNullAssertionExpression(receiver)
			}
		}
		while (isInvoke()) {
			receiver = parseInvokeExpression(receiver)
		}
		while (ctx.match(PzlTokenType.BANG)) {
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

fun isAccessOperator(ctx: PzlParserContext): Boolean {
	return accessTokenTypes.any { ctx.match(it) }
}