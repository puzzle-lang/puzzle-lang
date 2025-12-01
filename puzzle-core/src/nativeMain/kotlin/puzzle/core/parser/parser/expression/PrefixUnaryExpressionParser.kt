package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.ast.expression.PrefixUnaryExpression
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.symbol.toOperator

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePrefixUnaryExpression(): PrefixUnaryExpression {
    val operator = cursor.previous.type.toOperator()
    val name = parseIdentifierName(IdentifierNameTarget.PREFIX_UNARY)
    val expression = IdentifierExpression(name)
    return PrefixUnaryExpression(operator, expression)
}