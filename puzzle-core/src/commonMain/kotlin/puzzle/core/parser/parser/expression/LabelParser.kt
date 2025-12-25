package puzzle.core.parser.parser.expression

import puzzle.core.parser.PzlTokenCursor
import puzzle.core.token.kinds.PzlTokenKind
import puzzle.core.token.kinds.SymbolKind.HASH

fun PzlTokenCursor.matchLabel(kind: PzlTokenKind): Boolean {
	if (!checkIdentifier()) return false
	if (nextOrNull?.kind != HASH) return false
	if (offsetOrNull(2)?.kind != kind) return false
	this.advance(3)
	return true
}