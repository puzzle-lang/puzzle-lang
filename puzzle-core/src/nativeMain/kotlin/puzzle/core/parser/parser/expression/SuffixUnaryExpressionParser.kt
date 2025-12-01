package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IdentifierExpression
import puzzle.core.parser.ast.expression.SuffixUnaryExpression
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.symbol.toOperator

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseSuffixUnaryExpression(): SuffixUnaryExpression {
    val name = parseIdentifierName(IdentifierNameTarget.SUFFIX_UNARY)
    val expression = IdentifierExpression(name)
    cursor.advance()
    val operator = cursor.previous.type.toOperator()
    return SuffixUnaryExpression(expression, operator)
}