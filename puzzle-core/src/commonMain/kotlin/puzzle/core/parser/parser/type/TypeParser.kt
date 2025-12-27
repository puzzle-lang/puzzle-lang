package puzzle.core.parser.parser.type

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.type.NamedType
import puzzle.core.parser.parser.argument.parseTypeArguments
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.tryParseIdentifierString
import puzzle.core.token.kinds.AccessKind.DOT
import puzzle.core.token.kinds.ContextualKind.WITH
import puzzle.core.token.kinds.SeparatorKind.COMMA

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseNamedType(): NamedType {
	val start = cursor.current.location
	val segments = buildList {
		do {
			val segment = tryParseIdentifierString(IdentifierTarget.TYPE_REFERENCE)
			if (segment != null) {
				this += segment
			} else {
				if (this.isEmpty()) {
					syntaxError("类型识别错误", cursor.current)
				}
				cursor.retreat()
				break
			}
		} while (cursor.match(DOT))
	}
	val typeArguments = parseTypeArguments()
	val location = start span cursor.previous.location
	return NamedType(segments, location, typeArguments)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseWithTypes(): List<NamedType> {
	if (!cursor.match(WITH)) return emptyList()
	return buildList {
		do {
			this += parseNamedType()
		} while (cursor.match(COMMA))
	}
}