package puzzle.core.parser.parser.parameter

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.Quantifier
import puzzle.core.parser.ast.parameter.TypeExpansion
import puzzle.core.parser.ast.parameter.TypeExpansionKind
import puzzle.core.parser.ast.parameter.VarargKind
import puzzle.core.token.kinds.OperatorKind.PLUS
import puzzle.core.token.kinds.OperatorKind.STAR
import puzzle.core.token.kinds.SymbolKind.TRIPLE_DOT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseQuantifier(): Quantifier? {
	return when {
		cursor.match(PLUS, TRIPLE_DOT) -> Quantifier(TypeExpansionKind.NOT_EMPTY, cursor.previous.location)
		cursor.match(TRIPLE_DOT) -> Quantifier(TypeExpansionKind.ALLOW_EMPTY, cursor.previous.location)
		cursor.match(PLUS) -> Quantifier(VarargKind.NOT_EMPTY, cursor.previous.location)
		cursor.match(STAR) -> Quantifier(VarargKind.ALLOW_EMPTY, cursor.previous.location)
		else -> null
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeExpansion(): TypeExpansion? {
	return when {
		cursor.match(PLUS, TRIPLE_DOT) -> TypeExpansion(TypeExpansionKind.NOT_EMPTY, cursor.previous.location)
		cursor.match(TRIPLE_DOT) -> TypeExpansion(TypeExpansionKind.ALLOW_EMPTY, cursor.previous.location)
		else -> null
	}
}