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
import puzzle.core.model.SourceLocation

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
	val header = DeclarationHeader(docComment, annotationCalls, typeSpec, contextSpec, modifiers)
	val start = header.start
	return matcher.parse(header, start, isMember = false)
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
	val header = DeclarationHeader(docComment, annotationCalls, typeSpec, contextSpec, modifiers)
	val start = header.start
	return matcher.parse(header, start, isMember = true)
}

context(cursor: PzlTokenCursor)
private val DeclarationHeader.start: SourceLocation
	get() {
		if (docComment != null) return docComment.location
		if (annotationCalls.isNotEmpty()) return annotationCalls.first().location
		if (typeSpec != null) return typeSpec.location
		if (contextSpec != null) return contextSpec.location
		if (modifiers.isNotEmpty()) return modifiers.first().location
		return cursor.previous.location
	}