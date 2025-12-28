package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parsePropertyDeclaration
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.ModifierKind.VAL
import puzzle.core.token.kinds.ModifierKind.VAR

object PropertyDeclarationMatcher : DeclarationMatcher<PropertyDeclaration> {
	
	override val typeTarget = TypeTarget.PROPERTY
	
	override val modifierTarget = ModifierTarget.PROPERTY
	
	override val contextTarget = ContextTarget.PROPERTY
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		val kind = cursor.previous.kind
		return kind == VAR || kind == VAL
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): PropertyDeclaration {
		return parsePropertyDeclaration(header, start)
	}
}