package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.parser.binding.context.parseContextSpec
import puzzle.core.parser.parser.binding.type.parseTypeSpec
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.symbol.Modifier

sealed interface TopLevelDeclarationMatcher<out D : Declaration> {

    fun match(cursor: PzlTokenCursor): Boolean

    context(_: PzlContext)
    fun parse(
	    cursor: PzlTokenCursor,
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

context(_: PzlContext)
fun parseTopLevelDeclaration(cursor: PzlTokenCursor): Declaration {
    val typeSpec = parseTypeSpec(cursor)
    val contextSpec = parseContextSpec(cursor)
    val modifiers = parseModifiers(cursor)
    val matcher = matchers.find { it.match(cursor) }
        ?: syntaxError(
            message = if (cursor.current.type == PzlTokenType.EOF) "结尾缺少 '}'" else "未知的顶层声明",
            token = cursor.current
        )
    return matcher.parse(cursor, typeSpec, contextSpec, modifiers)
}