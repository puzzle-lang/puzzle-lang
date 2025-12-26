package puzzle.core.parser.matcher.declaration.toplevel

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget

sealed interface DeclarationMatcher<out D : Declaration> {
	
	companion object {
		
		val matchers = arrayOf(
			FunDeclarationMatcher,
			PropertyDeclarationMatcher,
			ClassDeclarationMatcher,
			ObjectDeclarationMatcher,
			TraitDeclarationMatcher,
			MixinDeclarationMatcher,
			StructDeclarationMatcher,
			EnumDeclarationMatcher,
			AnnotationDeclarationMatcher,
			ExtensionDeclarationMatcher
		)
	}
	
	val typeTarget: TypeTarget
	
	val modifierTarget: ModifierTarget
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(header: DeclarationHeader, start: SourceLocation): D
}