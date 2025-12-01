package puzzle.core.parser.parser.binding.parameter

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseLambdaParameters(): List<Parameter> {
    val parameters = mutableListOf<Parameter>()
    while (!cursor.match(PzlTokenType.RPAREN)) {
        parameters += parseLambdaParameter()
        if (!cursor.check(PzlTokenType.RPAREN)) {
            cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
        }
    }
    return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseLambdaParameter(): Parameter {
    val modifiers = parseModifiers()
    modifiers.check(ModifierTarget.LAMBDA_PARAMETER)
    return parseParameter(
        isSupportedTypeOnly = true,
        isSupportedLambdaType = true
    )
}