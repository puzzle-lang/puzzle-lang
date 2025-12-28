package puzzle.core.parser.matcher.declaration.member

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.FunDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseFunDeclaration
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.FUN

object MemberFunDeclarationMatcher : MemberDeclarationMatcher<FunDeclaration> {
	
	override val typeTarget = TypeTarget.FUN
	
	override val modifierTarget = ModifierTarget.MEMBER_FUN
	
	override val contextTarget = ContextTarget.FUN
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(FUN)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): FunDeclaration {
		return parseFunDeclaration(header, start)
	}
}