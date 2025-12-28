package puzzle.core.parser.matcher.declaration.member

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.parameter.context.ContextTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget

sealed interface MemberDeclarationMatcher<out D : Declaration> {
	
	companion object {
		
		val matchers = arrayOf(
			MemberFunDeclarationMatcher,
			MemberPropertyDeclarationMatcher,
			MemberClassDeclarationMatcher,
			MemberObjectDeclarationMatcher,
			MemberTraitDeclarationMatcher,
			MemberMixinDeclarationMatcher,
			MemberStructDeclarationMatcher,
			MemberEnumDeclarationMatcher,
			MemberAnnotationDeclarationMatcher,
			MemberExtensionDeclarationMatcher,
			TypealiasMemberDeclarationMatcher,
			MemberCtorDeclarationMatcher,
			MemberInitDeclarationMatcher
		)
	}
	
	val typeTarget: TypeTarget
	
	val modifierTarget: ModifierTarget
	
	val contextTarget: ContextTarget
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(header: DeclarationHeader, start: SourceLocation): D
}