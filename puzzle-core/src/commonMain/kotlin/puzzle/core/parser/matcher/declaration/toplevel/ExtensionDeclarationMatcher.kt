package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseExtensionDeclaration
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.EXTENSION

object ExtensionDeclarationMatcher : DeclarationMatcher<ExtensionDeclaration> {
	
	override val typeTarget = TypeTarget.EXTENSION
	
	override val modifierTarget = ModifierTarget.EXTENSION
	
	override val contextTarget = ContextTarget.EXTENSION
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(EXTENSION)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): ExtensionDeclaration {
		return parseExtensionDeclaration(header, start)
	}
}