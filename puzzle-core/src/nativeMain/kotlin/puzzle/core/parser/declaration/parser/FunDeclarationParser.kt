package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.constants.PzlTypes
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlParser
import puzzle.core.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.binding.parameter.parser.parseFunParameters
import puzzle.core.parser.declaration.ExtensionReceiver
import puzzle.core.parser.declaration.FunDeclaration
import puzzle.core.parser.node.NamedType
import puzzle.core.parser.node.TypeReference
import puzzle.core.parser.node.parser.TypeReferenceParser
import puzzle.core.parser.statement.Statement
import puzzle.core.parser.statement.matcher.parseStatement

class FunDeclarationParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<FunDeclarationParser>(::FunDeclarationParser)
	
	context(_: PzlContext)
	fun parse(modifiers: List<Modifier>): FunDeclaration {
		val extensionReceiver = parseExtensionReceiver()
		val funName = cursor.previous.value
		
		val parameters = parseFunParameters(cursor)
		val returnTypes = mutableListOf<TypeReference>()
		if (cursor.match(PzlTokenType.COLON)) {
			do {
				returnTypes += TypeReferenceParser.of(cursor).parse(isSupportedLambdaType = true)
			} while (cursor.match(PzlTokenType.COMMA))
		} else {
			returnTypes += TypeReference(PzlTypes.Unit)
		}
		
		if (!cursor.match(PzlTokenType.LBRACE)) {
			return FunDeclaration(
				name = funName,
				parameters = parameters,
				modifiers = modifiers,
				returnTypes = returnTypes,
				extensionReceiver = extensionReceiver
			)
		}
		val statements = mutableListOf<Statement>()
		while (!cursor.match(PzlTokenType.RBRACE)) {
			statements += parseStatement(cursor)
		}
		return FunDeclaration(
			name = funName,
			parameters = parameters,
			modifiers = modifiers,
			returnTypes = returnTypes,
			extensionReceiver = extensionReceiver,
			statements = statements
		)
	}
	
	context(_: PzlContext)
	private fun parseExtensionReceiver(): ExtensionReceiver? {
		cursor.expect(PzlTokenType.IDENTIFIER, "函数缺少名称")
		if (cursor.current.type == PzlTokenType.LPAREN) {
			return null
		}
		val segments = mutableListOf(cursor.previous.value)
		while (cursor.match(PzlTokenType.DOT)) {
			cursor.expect(PzlTokenType.IDENTIFIER, "'.' 后必须跟上标识符")
			segments += cursor.previous.value
			when {
				cursor.match(PzlTokenType.QUESTION_DOT) -> {
					cursor.expect(PzlTokenType.IDENTIFIER, "函数缺少名称")
					return ExtensionReceiver(
						type = TypeReference(
							type = NamedType(segments),
							isNullable = true
						)
					)
				}
				
				cursor.match(PzlTokenType.ELVIS) -> {
					syntaxError("此处不允许使用 Elvis 运算符 '?:'，而是分开的 '?' 和 ':'", cursor.previous)
				}
				
				cursor.match(PzlTokenType.QUESTION, PzlTokenType.COLON) -> {
					val type = TypeReference(
						type = NamedType(segments),
						isNullable = true
					)
					val superTrait = parseSuperTrait()
					return ExtensionReceiver(type, superTrait)
				}
				
				cursor.match(PzlTokenType.COLON) -> {
					val type = TypeReference(
						type = NamedType(segments),
						isNullable = true
					)
					val superTrait = parseSuperTrait()
					return ExtensionReceiver(type, superTrait)
				}
				
				cursor.current.type == PzlTokenType.LPAREN -> {
					return ExtensionReceiver(
						type = TypeReference(
							type = NamedType(segments.dropLast(1)),
						)
					)
				}
			}
		}
		cursor.expect(PzlTokenType.COLON, "扩展函数并实现特征缺少 ':'")
		val type = TypeReference(
			type = NamedType(segments.dropLast(1)),
		)
		val superTrait = parseSuperTrait()
		return ExtensionReceiver(type, superTrait)
	}
	
	context(_: PzlContext)
	private fun parseSuperTrait(): TypeReference {
		val segments = mutableListOf<String>()
		do {
			cursor.expect(PzlTokenType.IDENTIFIER, "缺少标识符")
			segments += cursor.previous.value
			if (cursor.match(PzlTokenType.QUESTION_DOT)) {
				cursor.expect(PzlTokenType.IDENTIFIER, "函数缺少名称")
				return TypeReference(
					type = NamedType(segments.dropLast(1)),
					isNullable = true
				)
			}
		} while (cursor.match(PzlTokenType.DOT))
		if (segments.size == 1) {
			syntaxError("':' 后必须跟特征类型", cursor.previous)
		}
		return TypeReference(
			type = NamedType(segments.dropLast(1)),
		)
	}
}