package puzzle.core.parser.matcher.declaration.member

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseEnumDeclaration
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.ENUM

object MemberEnumDeclarationMatcher : MemberDeclarationMatcher<EnumDeclaration> {
	
	override val typeTarget = TypeTarget.ENUM
	
	override val modifierTarget = ModifierTarget.MEMBER_ENUM
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean = cursor.match(ENUM)
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
	): EnumDeclaration = parseEnumDeclaration(header, start)
}