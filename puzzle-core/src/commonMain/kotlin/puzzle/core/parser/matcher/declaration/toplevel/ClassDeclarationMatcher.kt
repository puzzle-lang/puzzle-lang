package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.declaration.parseClassDeclaration
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.kinds.DeclarationKind.CLASS

object ClassDeclarationMatcher : DeclarationMatcher<ClassDeclaration> {
	
	override val typeTarget = TypeTarget.CLASS
	
	override val modifierTarget = ModifierTarget.CLASS
	
	override val contextTarget = ContextTarget.CLASS
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(CLASS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(header: DeclarationHeader, start: SourceLocation): ClassDeclaration {
		return parseClassDeclaration(header, start)
	}
}