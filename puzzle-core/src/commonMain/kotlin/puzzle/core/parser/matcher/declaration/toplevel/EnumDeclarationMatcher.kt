package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseEnumDeclaration
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.ENUM

object EnumDeclarationMatcher : DeclarationMatcher<EnumDeclaration> {
	
	override val typeTarget = TypeTarget.ENUM
	
	override val modifierTarget = ModifierTarget.ENUM
	
	override val contextTarget = ContextTarget.ENUM
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(ENUM)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): EnumDeclaration {
		return parseEnumDeclaration(header, start)
	}
}