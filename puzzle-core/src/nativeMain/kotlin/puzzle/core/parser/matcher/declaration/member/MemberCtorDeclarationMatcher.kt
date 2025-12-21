package puzzle.core.parser.matcher.declaration.member

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.CtorDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseCtorDeclaration
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind

object MemberCtorDeclarationMatcher : MemberDeclarationMatcher<CtorDeclaration> {
	
	override val typeTarget = TypeTarget.CTOR
	
	override val modifierTarget = ModifierTarget.CTOR
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.CTOR)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): CtorDeclaration {
		return parseCtorDeclaration(header, start)
	}
}