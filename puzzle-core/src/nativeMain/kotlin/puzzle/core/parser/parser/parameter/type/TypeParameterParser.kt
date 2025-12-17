package puzzle.core.parser.parser.parameter.type

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.parameter.TypeParameter
import puzzle.core.parser.ast.parameter.Variance
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.model.SourceLocation
import puzzle.core.token.kinds.*

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeParameters(): List<TypeParameter> {
	val parameters = mutableListOf<TypeParameter>()
	do {
		parameters += parseTypeParameter()
		if (!cursor.check(OperatorKind.GT)) {
			cursor.expect(SeparatorKind.COMMA, "缺少 ','")
		}
	} while (!cursor.match(OperatorKind.GT))
	return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseTypeParameter(): TypeParameter {
	val start = cursor.position
	val variance = parseVariance()
	val name = parseIdentifierExpression(IdentifierTarget.TYPE_PARAMETER)
	val bounds = if (cursor.match(SymbolKind.COLON)) {
		buildList<TypeReference> {
			do {
				val type = parseTypeReference(isSupportedNullable = true)
				if (this.isNotEmpty()) {
					val last = this.last()
					if (last.isNullable != type.isNullable) {
						val token = if (type.isNullable) {
							cursor.previous
						} else {
							val type = type.type as NamedType
							cursor.offset(offset = -type.segments.size * 2 - 1)
						}
						syntaxError("泛型上界指定多个类型时，可空需要一致", token)
					}
				}
				this += type
			} while (cursor.match(OperatorKind.BIT_AND))
		}
	} else emptyList()
	val defaultType = if (cursor.match(AssignmentKind.ASSIGN)) {
		parseTypeReference(isSupportedNullable = bounds.isEmpty() || bounds.first().isNullable)
	} else null
	val end = cursor.position
	return TypeParameter(
		name = name,
		variance = variance,
		bounds = bounds,
		defaultType = defaultType,
		location = SourceLocation(start, end)
	)
}

context(cursor: PzlTokenCursor)
private fun parseVariance(): Variance? {
	return when {
		cursor.match(VarianceKind.IN) -> Variance(VarianceKind.IN, cursor.previous.location)
		cursor.match(VarianceKind.OUT) -> Variance(VarianceKind.OUT, cursor.previous.location)
		else -> null
	}
}