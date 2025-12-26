package puzzle.core.parser.parser.parameter.type

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.type.NamedType
import puzzle.core.parser.ast.type.TypeReference
import puzzle.core.parser.ast.parameter.TypeParameter
import puzzle.core.parser.ast.parameter.Variance
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.OperatorKind.BIT_AND
import puzzle.core.token.kinds.OperatorKind.GT
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON
import puzzle.core.token.kinds.VarianceKind.IN
import puzzle.core.token.kinds.VarianceKind.OUT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeParameters(): List<TypeParameter> {
	val parameters = mutableListOf<TypeParameter>()
	do {
		parameters += parseTypeParameter()
		if (!cursor.check(GT)) {
			cursor.expect(COMMA, "缺少 ','")
		}
	} while (!cursor.match(GT))
	return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseTypeParameter(): TypeParameter {
	val start = cursor.position
	val variance = parseVariance()
	val name = parseIdentifier(IdentifierTarget.TYPE_PARAMETER)
	val bounds = if (cursor.match(COLON)) {
		buildList<TypeReference> {
			do {
				val type = parseTypeReference(allowNullable = true)
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
			} while (cursor.match(BIT_AND))
		}
	} else emptyList()
	val defaultType = if (cursor.match(ASSIGN)) {
		parseTypeReference(allowNullable = bounds.isEmpty() || bounds.first().isNullable)
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
		cursor.match(IN) -> Variance(IN, cursor.previous.location)
		cursor.match(OUT) -> Variance(OUT, cursor.previous.location)
		else -> null
	}
}