package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.ModifierKind

sealed interface DeclarationMatcher<out D : Declaration> {
	
	companion object {
		
		val matchers = arrayOf(
			FunDeclarationMatcher,
			PropertyDeclarationMatcher,
			ClassDeclarationMatcher,
			UniqueDeclarationMatcher,
			TraitDeclarationMatcher,
			StructDeclarationMatcher,
			EnumDeclarationMatcher,
			AnnotationDeclarationMatcher,
			ExtensionDeclarationMatcher
		)
	}
	
	val typeTarget: TypeTarget
	
	val memberModifierTarget: ModifierTarget
	
	val topLevelModifierTarget: ModifierTarget
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(
		typeSpec: TypeSpec?,
		contextSpec: ContextSpec?,
		modifiers: List<ModifierKind>,
		annotationCalls: List<AnnotationCall>,
		isMember: Boolean
	): D
}