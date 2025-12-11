package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.declaration.parseClassDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.DeclarationKind
import puzzle.core.token.ModifierKind

object ClassDeclarationMatcher : DeclarationMatcher<ClassDeclaration> {
	
	override val typeTarget = TypeTarget.CLASS
	
	override val memberModifierTarget = ModifierTarget.MEMBER_CLASS
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_CLASS
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.CLASS)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		typeSpec: TypeSpec?,
		contextSpec: ContextSpec?,
		modifiers: List<ModifierKind>,
		annotationCalls: List<AnnotationCall>,
		isMember: Boolean
	): ClassDeclaration = parseClassDeclaration(typeSpec, contextSpec, modifiers, annotationCalls)
}