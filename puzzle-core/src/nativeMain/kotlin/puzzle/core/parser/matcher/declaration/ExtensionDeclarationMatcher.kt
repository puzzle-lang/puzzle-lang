package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.parser.declaration.parseExtensionDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.model.SourceLocation
import puzzle.core.token.kinds.DeclarationKind
import puzzle.core.token.kinds.DeclarationKind.EXTENSION

object ExtensionDeclarationMatcher : DeclarationMatcher<ExtensionDeclaration> {
	
	override val typeTarget = TypeTarget.EXTENSION
	
	override val memberModifierTarget = ModifierTarget.MEMBER_EXTENSION
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_EXTENSION
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(EXTENSION)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		header: DeclarationHeader,
		start: SourceLocation,
		isMember: Boolean
	): ExtensionDeclaration = parseExtensionDeclaration(header, start)
}