package puzzle.core.parser.matcher.declaration.member

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseStructDeclaration
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.STRUCT

object MemberStructDeclarationMatcher : MemberDeclarationMatcher<StructDeclaration> {
	
	override val typeTarget = TypeTarget.STRUCT
	
	override val modifierTarget = ModifierTarget.MEMBER_STRUCT
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean = cursor.match(STRUCT)
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
	): StructDeclaration = parseStructDeclaration(header, start)
}