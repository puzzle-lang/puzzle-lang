package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.parser.declaration.parseTraitDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.DeclarationKind

object TraitDeclarationMatcher : DeclarationMatcher<TraitDeclaration> {
	
	override val typeTarget = TypeTarget.TRAIT
	
	override val memberModifierTarget = ModifierTarget.MEMBER_TRAIT
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_TRAIT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.TRAIT)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		isMember: Boolean
	): TraitDeclaration = parseTraitDeclaration(header)
}