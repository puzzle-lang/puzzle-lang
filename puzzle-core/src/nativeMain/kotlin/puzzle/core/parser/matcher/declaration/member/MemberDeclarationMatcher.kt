package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.symbol.Modifier

sealed interface MemberDeclarationMatcher<out D : Declaration> {
	
	fun match(cursor: PzlTokenCursor): Boolean
	
	context(_: PzlContext)
	fun check(cursor: PzlTokenCursor, modifiers: List<Modifier>)
	
	context(_: PzlContext)
	fun parse(cursor: PzlTokenCursor, modifiers: List<Modifier>): D
}

private val matchers = listOf(
	MemberFunDeclarationMatcher,
	MemberClassDeclarationMatcher,
	MemberUniqueDeclarationMatcher,
	MemberTraitDeclarationMatcher,
	MemberStructDeclarationMatcher,
	MemberEnumDeclarationMatcher,
	MemberAnnotationDeclarationMatcher,
	MemberExtensionDeclarationMatcher
)

context(_: PzlContext)
fun parseMemberDeclaration(cursor: PzlTokenCursor): Declaration {
	val modifiers = parseModifiers(cursor)
	val matcher = matchers.find { it.match(cursor) }
		?: if (cursor.current.type == PzlTokenType.EOF) {
			syntaxError("结尾缺少 '}'", cursor.current)
		} else {
			syntaxError("未知的成员声明", cursor.current)
		}
	matcher.check(cursor, modifiers)
	return matcher.parse(cursor, modifiers)
}