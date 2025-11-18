package puzzle.core.parser.declaration.matcher.member

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.Modifier
import puzzle.core.parser.checkSupportedDeclarationModifiers
import puzzle.core.parser.declaration.AnnotationDeclaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.parser.AnnotationDeclarationParser

object MemberAnnotationDeclarationMatcher : MemberDeclarationMatcher<AnnotationDeclaration> {
	
	override fun match(ctx: PzlParserContext): Boolean {
		return ctx.match(PzlTokenType.ANNOTATION)
	}
	
	context(_: PzlContext)
	override fun check(
		ctx: PzlParserContext,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "注解",
		)
	}
	
	context(_: PzlContext)
	override fun parse(
		ctx: PzlParserContext,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): AnnotationDeclaration {
		return AnnotationDeclarationParser(ctx).parse(modifiers)
	}
}