package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.constants.PzlTypes
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.declaration.FunDeclaration
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.node.parser.TypeReferenceParser
import puzzle.core.parser.parameter.parser.parseFunParameters
import puzzle.core.parser.statement.Statement
import puzzle.core.parser.statement.matcher.parseStatement

class FunDeclarationParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): FunDeclaration {
		ctx.expect(PzlTokenType.IDENTIFIER, "函数缺少名称")
		val funName = ctx.previous.value
		val parameters = parseFunParameters(ctx)
		
		val returnTypes = mutableListOf<TypeReference>()
		if (ctx.match(PzlTokenType.COLON)) {
			do {
				returnTypes += TypeReferenceParser(ctx).parse()
			} while (ctx.match(PzlTokenType.COMMA))
		} else {
			returnTypes += TypeReference(PzlTypes.Unit)
		}
		
		if (!ctx.match(PzlTokenType.LBRACE)) {
			return FunDeclaration(
				name = funName,
				parameters = parameters,
				modifiers = modifiers.toSet(),
				returnTypes = returnTypes
			)
		}
		val statements = mutableListOf<Statement>()
		while (!ctx.match(PzlTokenType.RBRACE)) {
			statements += parseStatement(ctx)
		}
		return FunDeclaration(
			name = funName,
			parameters = parameters,
			modifiers = modifiers.toSet(),
			returnTypes = returnTypes,
			statements = statements
		)
	}
}