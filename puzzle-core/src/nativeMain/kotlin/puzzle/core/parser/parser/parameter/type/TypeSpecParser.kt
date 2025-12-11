package puzzle.core.parser.parser.parameter.type

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.TokenRange
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.token.ContextualKind
import puzzle.core.token.OperatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeSpec(): TypeSpec? {
	return when {
		cursor.match(ContextualKind.REIFIED, ContextualKind.TYPE) -> parseTypeSpec(true)
		cursor.match(ContextualKind.TYPE) -> parseTypeSpec(false)
		else -> null
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun TypeSpec.check(target: TypeTarget) {
	if (!target.isSupportedType) {
		syntaxError("${target.displayName}不支持泛型", cursor[this.location.start])
	}
	if (!target.isSupportedVariance) {
		this.parameters.forEach {
			if (it.variance != null) {
				syntaxError("${target.displayName}不支持使用 '${it.variance.value}'", cursor[it.location.start])
			}
		}
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseTypeSpec(reified: Boolean): TypeSpec {
	val start = if (reified) cursor.position - 2 else cursor.position - 1
	cursor.expect(OperatorKind.LT, "'type' 后必须跟 '<'")
	val parameters = parseTypeParameters()
	val end = cursor.position
	return TypeSpec(
		reified = reified,
		parameters = parameters,
		location = TokenRange(start, end),
	)
}