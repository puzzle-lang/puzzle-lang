package puzzle.core.parser.matcher.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.UniqueDeclaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.declaration.parseUniqueDeclaration
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.token.DeclarationKind
import puzzle.core.token.ModifierKind

object UniqueDeclarationMatcher : DeclarationMatcher<UniqueDeclaration> {
	
	override val typeTarget = TypeTarget.UNIQUE
	
	override val memberModifierTarget = ModifierTarget.MEMBER_UNIQUE
	
	override val topLevelModifierTarget = ModifierTarget.TOP_LEVEL_UNIQUE
	
	context(cursor: PzlTokenCursor)
	override fun match(): Boolean {
		return cursor.match(DeclarationKind.UNIQUE)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(
		typeSpec: TypeSpec?,
		contextSpec: ContextSpec?,
		modifiers: List<ModifierKind>,
		annotationCalls: List<AnnotationCall>,
		isMember: Boolean,
	): UniqueDeclaration = parseUniqueDeclaration(contextSpec, modifiers, annotationCalls, isMember)
}