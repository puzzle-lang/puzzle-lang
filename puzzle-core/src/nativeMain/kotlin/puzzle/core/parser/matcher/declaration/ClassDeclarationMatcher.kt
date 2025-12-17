package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.parser.declaration.parseClassDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.SourceLocation
import puzzle.core.token.kinds.DeclarationKind

object ClassDeclarationMatcher : DeclarationMatcher<ClassDeclaration> {
	
	override val typeTarget = TypeTarget.CLASS
	
	override val memberModifierTarget = ModifierTarget.MEMBER_CLASS
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_CLASS
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.CLASS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
		isMember: Boolean
	): ClassDeclaration = parseClassDeclaration(header, start)
}