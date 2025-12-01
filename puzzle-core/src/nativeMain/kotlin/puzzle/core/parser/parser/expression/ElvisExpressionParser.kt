package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.ElvisExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.matcher.expression.parseCompleteExpression

context(_: PzlContext, _: PzlTokenCursor)
fun parseElvisExpression(left: Expression): ElvisExpression {
    val right = parseCompleteExpression()
    return ElvisExpression(left, right)
}