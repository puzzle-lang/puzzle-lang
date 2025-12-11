package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.declaration.parseExtensionDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.DeclarationKind
import puzzle.core.token.ModifierKind

object ExtensionDeclarationMatcher : DeclarationMatcher<ExtensionDeclaration> {
	
	override val typeTarget = TypeTarget.EXTENSION
	
	override val memberModifierTarget = ModifierTarget.MEMBER_EXTENSION
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_EXTENSION
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.EXTENSION)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		typeSpec: TypeSpec?,
		contextSpec: ContextSpec?,
		modifiers: List<ModifierKind>,
		annotationCalls: List<AnnotationCall>,
		isMember: Boolean
	): ExtensionDeclaration = parseExtensionDeclaration(typeSpec, contextSpec, modifiers, annotationCalls)
}