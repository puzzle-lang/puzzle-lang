package puzzle.core.parser.parser.binding.parameter

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.Parameter
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAnnotationParameters(): List<Parameter> {
    if (!cursor.match(PzlTokenType.LPAREN)) {
        return emptyList()
    }
    val parameters = mutableListOf<Parameter>()
    while (!cursor.match(PzlTokenType.RPAREN)) {
        parameters += parseAnnotationParameter()
        if (!cursor.check(PzlTokenType.RPAREN)) {
            cursor.expect(PzlTokenType.COMMA, "参数缺少 ','")
        }
    }
    return parameters
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseAnnotationParameter(): Parameter {
    val modifiers = parseModifiers()
    modifiers.check(ModifierTarget.ANNOTATION_PARAMETER)
    if (Modifier.VAL !in modifiers) {
        println(cursor.current.type)
        syntaxError("注解参数必须添加 'val' 修饰符", cursor.current)
    }
    return parseParameter()
}