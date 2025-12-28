package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.TypealiasDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseTypealiasDeclaration
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.TYPEALIAS

object TypealiasDeclarationMatcher : DeclarationMatcher<TypealiasDeclaration> {
	
	override val typeTarget = TypeTarget.TYPEALIAS
	
	override val modifierTarget = ModifierTarget.TYPEALIAS
	
	override val contextTarget = ContextTarget.TYPEALIAS
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(TYPEALIAS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): TypealiasDeclaration {
		return parseTypealiasDeclaration(header, start)
	}
}