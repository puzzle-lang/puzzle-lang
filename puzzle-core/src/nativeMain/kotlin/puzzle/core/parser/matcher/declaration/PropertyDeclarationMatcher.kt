package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.parser.declaration.parsePropertyDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.ModifierKind.VAL
import puzzle.core.token.kinds.ModifierKind.VAR

object PropertyDeclarationMatcher : DeclarationMatcher<PropertyDeclaration> {
	
	override val typeTarget = TypeTarget.PROPERTY
	
	override val memberModifierTarget = ModifierTarget.MEMBER_PROPERTY
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_PROPERTY
	
	private val kinds = arrayOf(VAR, VAL)
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		val type = cursor.previous.kind
		return kinds.any { type == it }
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
		isMember: Boolean,
	): PropertyDeclaration = parsePropertyDeclaration(header, start)
}