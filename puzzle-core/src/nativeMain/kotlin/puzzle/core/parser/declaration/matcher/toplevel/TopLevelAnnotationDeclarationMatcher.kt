package puzzle.core.parser.declaration.matcher.toplevel

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.AnnotationDeclaration
import puzzle.core.parser.declaration.parser.AnnotationDeclarationParser

object TopLevelAnnotationDeclarationMatcher : TopLevelDeclarationMatcher<AnnotationDeclaration> {
	
	override fun match(cursor: PzlTokenCursor): Boolean {
		return cursor.match(PzlTokenType.ANNOTATION)
	}
	
	context(_: PzlContext)
	override fun check(cursor: PzlTokenCursor, modifiers: Set<Modifier>) {
		checkSupportedDeclarationModifiers(cursor, modifiers, "顶层注解")
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, modifiers: Set<Modifier>): AnnotationDeclaration {
		return AnnotationDeclarationParser(cursor).parse(modifiers)
	}
}