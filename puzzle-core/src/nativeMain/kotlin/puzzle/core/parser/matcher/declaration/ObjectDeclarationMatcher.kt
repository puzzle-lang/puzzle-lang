package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ObjectDeclaration
import puzzle.core.parser.parser.declaration.parseObjectDeclaration
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.model.SourceLocation
import puzzle.core.token.kinds.DeclarationKind.OBJECT

object ObjectDeclarationMatcher : DeclarationMatcher<ObjectDeclaration> {
	
	override val typeTarget = TypeTarget.OBJECT
	
	override val memberModifierTarget = ModifierTarget.MEMBER_OBJECT
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_OBJECT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(OBJECT)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
		isMember: Boolean,
	): ObjectDeclaration = parseObjectDeclaration(header, start, isMember)
}