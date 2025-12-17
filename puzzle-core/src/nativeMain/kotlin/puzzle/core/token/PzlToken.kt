package puzzle.core.token

import puzzle.core.model.SourceLocation
import puzzle.core.token.kinds.PzlTokenKind

class PzlToken(
	val kind: PzlTokenKind,
	val location: SourceLocation,
) {
	val value: String
		get() = this.kind.value
}