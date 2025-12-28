package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.MixinDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseMixinDeclaration
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.MIXIN

object MixinDeclarationMatcher : DeclarationMatcher<MixinDeclaration> {
	
	override val typeTarget = TypeTarget.MIXIN
	
	override val modifierTarget = ModifierTarget.MIXIN
	
	override val contextTarget = ContextTarget.MIXIN
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(MIXIN)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): MixinDeclaration {
		return parseMixinDeclaration(header, start)
	}
}