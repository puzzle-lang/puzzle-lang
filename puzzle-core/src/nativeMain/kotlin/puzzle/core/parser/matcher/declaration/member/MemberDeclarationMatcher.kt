package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.GenericSpec
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.parser.binding.generic.parseGenericSpec
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.symbol.Modifier

sealed interface MemberDeclarationMatcher<out D : Declaration> {
	
	fun match(cursor: PzlTokenCursor): Boolean
	
	context(_: PzlContext)
	fun parse(
		cursor: PzlTokenCursor,
		genericSpec: GenericSpec?,
		modifiers: List<Modifier>
	): D
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
	val genericSpec = parseGenericSpec(cursor)
	val modifiers = parseModifiers(cursor)
	val matcher = matchers.find { it.match(cursor) }
		?: syntaxError(
			message = if (cursor.current.type == PzlTokenType.EOF) "结尾缺少 '}'" else "未知的成员声明",
			token = cursor.current
		)
	return matcher.parse(cursor, genericSpec, modifiers)
}