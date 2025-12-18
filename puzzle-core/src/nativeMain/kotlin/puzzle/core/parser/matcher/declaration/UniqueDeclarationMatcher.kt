package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.parser.declaration.parseUniqueDeclaration
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.model.SourceLocation
import puzzle.core.token.kinds.DeclarationKind.UNIQUE

object UniqueDeclarationMatcher : DeclarationMatcher<UniqueDeclaration> {
	
	override val typeTarget = TypeTarget.UNIQUE
	
	override val memberModifierTarget = ModifierTarget.MEMBER_UNIQUE
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_UNIQUE
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(UNIQUE)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
		isMember: Boolean,
	): UniqueDeclaration = parseUniqueDeclaration(header, start, isMember)
}