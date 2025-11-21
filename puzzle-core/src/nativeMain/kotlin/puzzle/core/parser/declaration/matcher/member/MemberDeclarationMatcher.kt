package puzzle.core.parser.declaration.matcher.member

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.getDisplayName

sealed interface MemberDeclarationMatcher<out D : Declaration> {
	
	fun match(cursor: PzlTokenCursor): Boolean
	
	context(_: PzlContext)
	fun check(
		cursor: PzlTokenCursor,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	)
	
	context(_: PzlContext)
	fun parse(
		cursor: PzlTokenCursor,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): D
}

private val matchers = listOf(
	MemberFunDeclarationMatcher,
	MemberClassDeclarationMatcher,
	MemberSingleDeclarationMatcher,
	MemberTraitDeclarationMatcher,
	MemberStructDeclarationMatcher,
	MemberEnumDeclarationMatcher,
	MemberAnnotationDeclarationMatcher,
	MemberExtensionDeclarationMatcher
)

context(_: PzlContext)
fun parseMemberDeclaration(
	cursor: PzlTokenCursor,
	parentTypeKind: TypeKind,
	parentModifiers: Set<Modifier>,
	modifiers: Set<Modifier>
): Declaration {
	val matcher = matchers.find { it.match(cursor) }
		?: if (cursor.current.type == PzlTokenType.EOF) {
			syntaxError("${parentTypeKind.getDisplayName()}缺少 '}'", cursor.current)
		} else {
			syntaxError("未知的成员声明", cursor.current)
		}
	matcher.check(cursor, parentTypeKind, parentModifiers, modifiers)
	return matcher.parse(cursor, parentTypeKind, modifiers)
}