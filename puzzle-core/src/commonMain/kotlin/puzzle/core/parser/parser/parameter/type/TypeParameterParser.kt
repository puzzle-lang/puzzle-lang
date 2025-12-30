package puzzle.core.parser.parser.parameter.type

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.parameter.TypeParameter
import puzzle.core.parser.ast.parameter.Variance
import puzzle.core.parser.ast.parameter.VarianceKind
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.parameter.parseTypeExpansion
import puzzle.core.parser.parser.type.parseTypeReference
import puzzle.core.token.kinds.AssignmentKind.ASSIGN
import puzzle.core.token.kinds.ContextualKind.OUT
import puzzle.core.token.kinds.OperatorKind.*
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTypeParameters(): List<TypeParameter> {
	val parameters = mutableListOf<TypeParameter>()
	do {
		parameters += parseTypeParameter()
		if (!cursor.check(GT)) {
			cursor.expect(COMMA, "泛型参数缺少 ','")
		}
	} while (!cursor.match(GT))
	return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseTypeParameter(): TypeParameter {
	val start = cursor.current.location
	val variance = parseVariance()
	val name = parseIdentifier(IdentifierTarget.TYPE_PARAMETER)
	val typeExpansion = parseTypeExpansion()
	val bounds = if (cursor.match(COLON)) {
		buildList {
			do {
				this += parseTypeReference()
			} while (cursor.match(BIT_AND))
		}
	} else emptyList()
	val allowNullable = bounds.all { it.isNullable }
	val defaultType = if (cursor.match(ASSIGN)) {
		if (typeExpansion != null) {
			syntaxError("泛型类型展开不允许设置默认值", cursor.previous)
		}
		parseTypeReference(allowNullable = allowNullable)
	} else null
	val end = cursor.previous.location
	return TypeParameter(
		name = name,
		variance = variance,
		bounds = bounds,
		defaultType = defaultType,
		typeExpansion = typeExpansion,
		location = start span end
	)
}

context(cursor: PzlTokenCursor)
private fun parseVariance(): Variance? {
	return when {
		cursor.match(IN) -> Variance(VarianceKind.IN, cursor.previous.location)
		cursor.match(OUT) -> Variance(VarianceKind.OUT, cursor.previous.location)
		else -> null
	}
}