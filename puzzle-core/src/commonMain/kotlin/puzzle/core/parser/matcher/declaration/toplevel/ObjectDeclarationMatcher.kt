package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ObjectDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseObjectDeclaration
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.OBJECT

object ObjectDeclarationMatcher : DeclarationMatcher<ObjectDeclaration> {
	
	override val typeTarget = TypeTarget.OBJECT
	
	override val modifierTarget = ModifierTarget.OBJECT
	
	override val contextTarget = ContextTarget.OBJECT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(OBJECT)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): ObjectDeclaration {
		return parseObjectDeclaration(header, start, isMember = false)
	}
}