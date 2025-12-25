package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.TraitDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseTraitDeclaration
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.TRAIT

object TraitDeclarationMatcher : DeclarationMatcher<TraitDeclaration> {
	
	override val typeTarget = TypeTarget.TRAIT
	
	override val modifierTarget = ModifierTarget.TRAIT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(TRAIT)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): TraitDeclaration {
		return parseTraitDeclaration(header, start)
	}
}