package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.matcher.declaration.DeclarationMatcher
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.parser.parser.parameter.context.parseContextSpec
import puzzle.core.parser.parser.parameter.type.check
import puzzle.core.parser.parser.parameter.type.parseTypeSpec
import puzzle.core.parser.parser.parseAnnotationCalls

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTopLevelDeclaration(): Declaration {
	val annotationCalls = parseAnnotationCalls()
	val typeSpec = parseTypeSpec()
	val contextSpec = parseContextSpec()
	val modifiers = parseModifiers()
	val matcher = DeclarationMatcher.matchers.find { it.match() } ?: syntaxError(
		message = if (cursor.isAtEnd()) "结尾缺少 '}'" else "未知的顶层声明",
		token = cursor.current
	)
	typeSpec?.check(matcher.typeTarget)
	modifiers.check(matcher.topLevelModifierTarget)
	return matcher.parse(typeSpec, contextSpec, modifiers, annotationCalls, isMember = false)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseMemberDeclaration(): Declaration {
	val annotationCalls = parseAnnotationCalls()
	val typeSpec = parseTypeSpec()
	val contextSpec = parseContextSpec()
	val modifiers = parseModifiers()
	val matcher = DeclarationMatcher.matchers.find { it.match() } ?: syntaxError(
		message = if (cursor.isAtEnd()) "结尾缺少 '}'" else "未知的成员声明",
		token = cursor.current
	)
	typeSpec?.check(matcher.typeTarget)
	modifiers.check(matcher.memberModifierTarget)
	return matcher.parse(typeSpec, contextSpec, modifiers, annotationCalls, isMember = true)
}