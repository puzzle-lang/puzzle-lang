package puzzle.core.parser.matcher.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.parser.parser.parameter.context.parseContextSpec
import puzzle.core.parser.parser.parameter.type.TypeTarget
import puzzle.core.parser.parser.parameter.type.check
import puzzle.core.parser.parser.parameter.type.parseTypeSpec
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.symbol.Modifier

sealed interface DeclarationMatcher<out D : Declaration> {
	
	val typeTarget: TypeTarget
	
	val memberModifierTarget: ModifierTarget
	
	val topLevelModifierTarget: ModifierTarget
	
	context(cursor: PzlTokenCursor)
	fun match(): Boolean
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	fun parse(
		typeSpec: TypeSpec?,
		contextSpec: ContextSpec?,
		modifiers: List<Modifier>,
		annotationCalls: List<AnnotationCall>,
		isMember: Boolean
	): D
}

private val matchers = arrayOf(
	FunDeclarationMatcher,
	PropertyDeclarationMatcher,
	ClassDeclarationMatcher,
	UniqueDeclarationMatcher,
	TraitDeclarationMatcher,
	StructDeclarationMatcher,
	EnumDeclarationMatcher,
	AnnotationDeclarationMatcher,
	ExtensionDeclarationMatcher
)

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTopLevelDeclaration(): Declaration {
	val annotationCalls = parseAnnotationCalls()
	val typeSpec = parseTypeSpec()
	val contextSpec = parseContextSpec()
	val modifiers = parseModifiers()
	val matcher = matchers.find { it.match() } ?: syntaxError(
		message = if (cursor.current.type == PzlTokenType.EOF) "结尾缺少 '}'" else "未知的顶层声明",
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
	val matcher = matchers.find { it.match() } ?: syntaxError(
		message = if (cursor.current.type == PzlTokenType.EOF) "结尾缺少 '}'" else "未知的成员声明",
		token = cursor.current
	)
	typeSpec?.check(matcher.typeTarget)
	modifiers.check(matcher.memberModifierTarget)
	return matcher.parse(typeSpec, contextSpec, modifiers, annotationCalls, isMember = true)
}