package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.declaration.parseEnumDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.DeclarationKind
import puzzle.core.token.ModifierKind

object EnumDeclarationMatcher : DeclarationMatcher<EnumDeclaration> {
	
	override val typeTarget = TypeTarget.ENUM
	
	override val memberModifierTarget = ModifierTarget.MEMBER_ENUM
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_ENUM
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.ENUM)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		typeSpec: TypeSpec?,
		contextSpec: ContextSpec?,
		modifiers: List<ModifierKind>,
		annotationCalls: List<AnnotationCall>,
		isMember: Boolean
	): EnumDeclaration = parseEnumDeclaration(typeSpec, contextSpec, modifiers, annotationCalls)
}