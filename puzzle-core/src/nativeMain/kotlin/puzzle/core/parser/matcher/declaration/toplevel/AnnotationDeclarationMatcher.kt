package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseAnnotationDeclaration
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.ANNOTATION

object AnnotationDeclarationMatcher : DeclarationMatcher<AnnotationDeclaration> {
	
	override val typeTarget = TypeTarget.ANNOTATION
	
	override val modifierTarget = ModifierTarget.ANNOTATION
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(ANNOTATION)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): AnnotationDeclaration {
		return parseAnnotationDeclaration(header, start)
	}
}