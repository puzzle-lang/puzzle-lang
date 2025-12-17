package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.DocComment
import puzzle.core.parser.ast.Modifier
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.model.SourceLocation

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
		header: DeclarationHeader,
		start: SourceLocation,
		isMember: Boolean,
	): D
}

class DeclarationHeader(
	val docComment: DocComment?,
	val annotationCalls: List<AnnotationCall>,
	val typeSpec: TypeSpec?,
	val contextSpec: ContextSpec?,
	val modifiers: List<Modifier>,
)