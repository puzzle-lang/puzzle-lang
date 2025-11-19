package puzzle.core.parser.declaration.parser

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.*
import puzzle.core.parser.declaration.ClassDeclaration
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.TypeKind
import puzzle.core.parser.declaration.matcher.member.parseMemberDeclaration
import puzzle.core.parser.parameter.parser.parseClassParameters

class ClassDeclarationParser(
	private val ctx: PzlParserContext,
) {
	
	context(_: PzlContext)
	fun parse(modifiers: Set<Modifier>): ClassDeclaration {
		ctx.expect(PzlTokenType.IDENTIFIER, "类缺少名称")
		val name = ctx.previous.value
		val classAccess = modifiers.access
		val constructorModifiers = mutableSetOf<Modifier>()
		constructorModifiers += getMemberAccessModifier(ctx, classAccess) {
			"主构造函数访问修饰符与类访问修饰符不兼容"
		}
		val parameters = parseClassParameters(ctx, classAccess)
		val superTypes = parseSuperTypes(ctx)
		if (!ctx.match(PzlTokenType.LBRACE)) {
			return ClassDeclaration(
				name = name,
				modifiers = modifiers,
				constructorModifiers = constructorModifiers,
				parameters = parameters,
				superTypes = superTypes
			)
		}
		
		val members = mutableListOf<Declaration>()
		while (!ctx.match(PzlTokenType.RBRACE)) {
			members += parseDeclaration(classAccess, modifiers)
		}
		
		return ClassDeclaration(
			name = name,
			modifiers = modifiers,
			constructorModifiers = constructorModifiers,
			parameters = parameters,
			superTypes = superTypes,
			members = members
		)
	}
	
	context(_: PzlContext)
	private fun parseDeclaration(
		classAccess: Modifier,
		classModifiers: Set<Modifier>,
	): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(ctx, classAccess) {
			"访问修饰符与类访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(ctx)
		return parseMemberDeclaration(
			ctx = ctx,
			parentTypeKind = TypeKind.CLASS,
			parentModifiers = classModifiers,
			modifiers = memberModifiers
		)
	}
}