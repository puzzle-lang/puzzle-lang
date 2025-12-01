package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.parser.binding.context.parseContextSpec
import puzzle.core.parser.parser.binding.type.parseTypeSpec
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.symbol.Modifier

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {

    context(cursor: PzlTokenCursor)
    fun match(): Boolean

    context(_: PzlContext, cursor: PzlTokenCursor)
    fun parse(
        typeSpec: TypeSpec?,
        contextSpec: ContextSpec?,
        modifiers: List<Modifier>,
    ): D
}

private val matchers = arrayOf(
    TopLevelFunDeclarationMatcher,
    TopLevelClassDeclarationMatcher,
    TopLevelUniqueDeclarationMatcher,
    TopLevelTraitDeclarationMatcher,
    TopLevelStructDeclarationMatcher,
    TopLevelEnumDeclarationMatcher,
    TopLevelAnnotationDeclarationMatcher,
    TopLevelExtensionDeclarationMatcher
)

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTopLevelDeclaration(): Declaration {
    val typeSpec = parseTypeSpec()
    val contextSpec = parseContextSpec()
    val modifiers = parseModifiers()
    val matcher = matchers.find { it.match() }
        ?: syntaxError(
            message = if (cursor.current.type == PzlTokenType.EOF) "结尾缺少 '}'" else "未知的顶层声明",
            token = cursor.current
        )
    return matcher.parse(typeSpec, contextSpec, modifiers)
}