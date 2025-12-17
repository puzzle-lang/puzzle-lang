package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.FunDeclaration
import puzzle.core.parser.parser.declaration.parseFunDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.SourceLocation
import puzzle.core.token.kinds.DeclarationKind

object FunDeclarationMatcher : DeclarationMatcher<FunDeclaration> {
	
	override val typeTarget = TypeTarget.FUN
	
	override val memberModifierTarget = ModifierTarget.MEMBER_FUN
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_FUN
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.FUN)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
		isMember: Boolean
	): FunDeclaration = parseFunDeclaration(header, start)
}