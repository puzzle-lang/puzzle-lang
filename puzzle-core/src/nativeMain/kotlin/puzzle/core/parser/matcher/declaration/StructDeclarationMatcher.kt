package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.parser.declaration.parseStructDeclaration
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.model.SourceLocation
import puzzle.core.token.kinds.DeclarationKind.STRUCT

object StructDeclarationMatcher : DeclarationMatcher<StructDeclaration> {
	
	override val typeTarget = TypeTarget.STRUCT
	
	override val memberModifierTarget = ModifierTarget.MEMBER_STRUCT
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_STRUCT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(STRUCT)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
		isMember: Boolean,
	): StructDeclaration = parseStructDeclaration(header, start)
}