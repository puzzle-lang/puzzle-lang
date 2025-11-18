package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.Argument
import puzzle.core.parser.expression.InvokeType
import puzzle.core.parser.expression.InvokeType.CALL
import puzzle.core.parser.expression.InvokeType.INDEX_ACCESS
import puzzle.core.parser.expression.matcher.parseCompleteExpression

context(_: PzlContext)
fun parseArguments(ctx: PzlParserContext, type: InvokeType): List<Argument> {
	if (ctx.match(type.endTokenType)) return emptyList()
	val arguments = mutableListOf<Argument>()
	do {
		arguments += CallArgumentParser(ctx).parse(type)
		if (!ctx.check(type.endTokenType)) {
			ctx.expect(PzlTokenType.COMMA, "参数缺少 ','")
		}
	} while (!ctx.match(type.endTokenType))
	return arguments
}

private class CallArgumentParser(
	private val ctx: PzlParserContext
) {
	context(_: PzlContext)
	fun parse(type: InvokeType): Argument {
		val name = if (ctx.peek(offset = 1)?.type == PzlTokenType.ASSIGN) {
			ctx.expect(PzlTokenType.IDENTIFIER, "型参名称必须为标识符")
			val name = ctx.previous.value
			ctx.expect(PzlTokenType.ASSIGN, "型参名称后必须为 '='")
			name
		} else null
		val expression = parseCompleteExpression(ctx)
		if (ctx.previous.type == PzlTokenType.SEMICOLON) {
			syntaxError("语法错误", ctx.previous)
		}
		val currentType = ctx.current.type
		if (currentType == PzlTokenType.COMMA) {
			return Argument(name, expression)
		}
		when (type) {
			CALL if (currentType == INDEX_ACCESS.endTokenType) -> syntaxError("函数参数表达式后只允许根 ')' 和 ','", ctx.current)
			INDEX_ACCESS if (currentType == CALL.endTokenType) -> syntaxError("索引访问参数表达式后只允许根 ']' 和 ','", ctx.current)
			else -> return Argument(name, expression)
		}
	}
}