package puzzle.core.frontend.token

import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.PzlTokenKind

class PzlToken(
	val kind: PzlTokenKind,
	val location: SourceLocation,
) {
	val value: String
		get() = this.kind.value
}