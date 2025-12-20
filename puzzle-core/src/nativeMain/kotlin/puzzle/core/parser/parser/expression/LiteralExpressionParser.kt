package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.*
import puzzle.core.token.kinds.LiteralKind
import puzzle.core.token.kinds.LiteralKind.*
import puzzle.core.token.kinds.LiteralKind.BooleanKind.FALSE
import puzzle.core.token.kinds.LiteralKind.BooleanKind.TRUE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseLiteralExpression(): LiteralExpression {
	val token = cursor.previous
	return when (val kind = token.kind as LiteralKind) {
		FALSE -> BooleanLiteral(false, token.location)
		TRUE -> BooleanLiteral(true, token.location)
		NULL -> NullLiteral(token.location)
		is CharKind -> CharLiteral(kind.value, token.location)
		is NumberKind -> NumberLiteral(kind.value, kind.system, kind.type, token.location)
		is StringKind -> StringLiteral(kind.value, token.location)
	}
}