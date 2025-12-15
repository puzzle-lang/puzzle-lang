package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.matcher.declaration.DeclarationMatcher
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.parser.parser.parameter.context.parseContextSpec
import puzzle.core.parser.parser.parameter.type.check
import puzzle.core.parser.parser.parameter.type.parseTypeSpec
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.parser.parser.parseDocComment

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTopLevelDeclaration(): Declaration {
	val docComment = parseDocComment()
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
	val header = DeclarationHeader.create(docComment, annotationCalls, typeSpec, contextSpec, modifiers)
	return matcher.parse(header, isMember = false)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseMemberDeclaration(): Declaration {
	val docComment = parseDocComment()
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
	val header = DeclarationHeader.create(docComment, annotationCalls, typeSpec, contextSpec, modifiers)
	return matcher.parse(header, isMember = true)
}