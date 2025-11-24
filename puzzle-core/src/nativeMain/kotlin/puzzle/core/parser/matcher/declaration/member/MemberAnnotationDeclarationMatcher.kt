package puzzle.core.parser.matcher.declaration.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.ast.declaration.NodeKind
import puzzle.core.parser.parser.checkModifiers
import puzzle.core.parser.parser.declaration.AnnotationDeclarationParser
import puzzle.core.symbol.Modifier

object MemberAnnotationDeclarationMatcher : MemberDeclarationMatcher<AnnotationDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.ANNOTATION)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: List<Modifier>) {
		checkModifiers(cursor, modifiers, NodeKind.MEMBER_ANNOTATION)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: List<Modifier>): AnnotationDeclaration {
		return AnnotationDeclarationParser.of(cursor).parse(modifiers)
	}
}