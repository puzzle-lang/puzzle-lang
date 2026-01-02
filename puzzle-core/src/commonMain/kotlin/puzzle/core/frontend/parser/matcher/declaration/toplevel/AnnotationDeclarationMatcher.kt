package puzzle.core.frontend.parser.matcher.declaration.toplevel

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.declaration.AnnotationDeclaration
import puzzle.core.frontend.parser.matcher.declaration.DeclarationHeader
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.declaration.parseAnnotationDeclaration
import puzzle.core.frontend.parser.parser.parameter.context.ContextTarget
import puzzle.core.frontend.parser.parser.parameter.type.TypeTarget
import puzzle.core.frontend.token.kinds.DeclarationKind.ANNOTATION

object AnnotationDeclarationMatcher : DeclarationMatcher<AnnotationDeclaration> {
	
	override val typeTarget = TypeTarget.ANNOTATION
	
	override val modifierTarget = ModifierTarget.ANNOTATION
	
	override val contextTarget = ContextTarget.ANNOTATION
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(ANNOTATION)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): AnnotationDeclaration {
		return parseAnnotationDeclaration(header, start)
	}
}