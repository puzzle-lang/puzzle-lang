package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.PropertyDeclaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.declaration.parsePropertyDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.ModifierKind

object PropertyDeclarationMatcher : DeclarationMatcher<PropertyDeclaration> {
	
	override val typeTarget = TypeTarget.PROPERTY
	
	override val memberModifierTarget = ModifierTarget.MEMBER_PROPERTY
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_PROPERTY
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		val type = cursor.previous.kind
		return type == ModifierKind.VAR || type == ModifierKind.VAL
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		typeSpec: TypeSpec?,
		contextSpec: ContextSpec?,
		modifiers: List<ModifierKind>,
		annotationCalls: List<AnnotationCall>,
		isMember: Boolean
	): PropertyDeclaration = parsePropertyDeclaration(typeSpec, contextSpec, modifiers, annotationCalls)
}