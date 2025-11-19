package puzzle.core.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType

enum class Modifier {
	PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
	VAR, VAL,
	OPEN, ABSTRACT, OVERRIDE, FINAL, CONST,
	IGNORE
}

private val accessModifiers = setOf(
	Modifier.PRIVATE,
	Modifier.PROTECTED,
	Modifier.FILE,
	Modifier.INTERNAL,
	Modifier.MODULE,
	Modifier.PUBLIC
)

val Set<Modifier>.access: Modifier
	get() {
		return accessModifiers.find { it in this }
			?: error("未知的修饰符")
	}

val Set<Modifier>.isOpen: Boolean
	get() = Modifier.OPEN in this

val Set<Modifier>.isAbstract: Boolean
	get() = Modifier.ABSTRACT in this

context(_: PzlContext)
fun getTopLevelAccessModifier(ctx: PzlParserContext): Modifier {
	return when {
		ctx.match(PzlTokenType.PRIVATE) -> Modifier.PRIVATE
		ctx.match(PzlTokenType.PROTECTED) -> syntaxError("顶层声明不支持 'protected' 修饰符", ctx.previous)
		ctx.match(PzlTokenType.FILE) -> syntaxError("顶层声明不支持 'file' 修饰符", ctx.previous)
		ctx.match(PzlTokenType.INTERNAL) -> Modifier.INTERNAL
		ctx.match(PzlTokenType.MODULE) -> Modifier.MODULE
		ctx.match(PzlTokenType.PUBLIC) -> Modifier.PUBLIC
		else -> Modifier.PUBLIC
	}
}

context(_: PzlContext)
fun getMemberAccessModifier(ctx: PzlParserContext, parentAccess: Modifier, errorMessage: () -> String): Modifier {
	val memberAccess = when {
		ctx.match(PzlTokenType.PRIVATE) -> Modifier.PRIVATE
		ctx.match(PzlTokenType.PROTECTED) -> Modifier.PROTECTED
		ctx.match(PzlTokenType.FILE) -> Modifier.FILE
		ctx.match(PzlTokenType.INTERNAL) -> Modifier.INTERNAL
		ctx.match(PzlTokenType.MODULE) -> Modifier.MODULE
		ctx.match(PzlTokenType.PUBLIC) -> Modifier.PUBLIC
		else -> null
	}
	return if (memberAccess != null) {
		if (!((parentAccess == Modifier.PRIVATE && memberAccess <= Modifier.FILE) || (memberAccess <= parentAccess))) {
			syntaxError(errorMessage(), ctx.previous)
		}
		memberAccess
	} else {
		getDefaultMemberAccessModifier(parentAccess)
	}
}

fun getDefaultMemberAccessModifier(parentAccess: Modifier): Modifier {
	return when (parentAccess) {
		Modifier.PRIVATE, Modifier.FILE -> Modifier.FILE
		Modifier.INTERNAL -> Modifier.INTERNAL
		Modifier.MODULE -> Modifier.MODULE
		Modifier.PUBLIC -> Modifier.PUBLIC
		else -> error("不支持的访问修饰符")
	}
}

context(_: PzlContext)
fun getClassParameterAccessModifier(ctx: PzlParserContext, classAccess: Modifier, errorMessage: () -> String): Modifier? {
	val access = when {
		ctx.match(PzlTokenType.PRIVATE) -> Modifier.PRIVATE
		ctx.match(PzlTokenType.PROTECTED) -> Modifier.PROTECTED
		ctx.match(PzlTokenType.FILE) -> Modifier.FILE
		ctx.match(PzlTokenType.INTERNAL) -> Modifier.INTERNAL
		ctx.match(PzlTokenType.MODULE) -> Modifier.MODULE
		ctx.match(PzlTokenType.PUBLIC) -> Modifier.PUBLIC
		else -> return null
	}
	if (!((classAccess == Modifier.PRIVATE && access <= Modifier.FILE) || (access <= classAccess))) {
		syntaxError(errorMessage(), ctx.previous)
	}
	return access
}

fun getDeclarationModifiers(ctx: PzlParserContext): Set<Modifier> {
	val modifiers = mutableSetOf<Modifier>()
	when {
		ctx.match(PzlTokenType.CONST) -> modifiers += Modifier.CONST
		ctx.match(PzlTokenType.OPEN) -> modifiers += Modifier.OPEN
		ctx.match(PzlTokenType.ABSTRACT) -> modifiers += Modifier.ABSTRACT
		ctx.match(PzlTokenType.OVERRIDE) -> modifiers += Modifier.OVERRIDE
		ctx.match(PzlTokenType.FINAL, PzlTokenType.OVERRIDE) -> {
			modifiers += Modifier.FINAL
			modifiers += Modifier.OVERRIDE
		}
	}
	return modifiers
}

context(_: PzlContext)
fun checkSupportedDeclarationModifiers(
	ctx: PzlParserContext,
	modifiers: Set<Modifier>,
	name: String,
	isSupportedConst: Boolean = false,
	isSupportedOpen: Boolean = false,
	isSupportedAbstract: Boolean = false,
	isSupportedFinalOverride: Boolean = false,
	isSupportedOverride: Boolean = false
) {
	when {
		Modifier.CONST in modifiers && !isSupportedConst -> syntaxError(
			"${name}不支持 'const' 修饰符",
			ctx.previous
		)
		
		Modifier.OPEN in modifiers && !isSupportedOpen -> syntaxError(
			"${name}不支持 'open' 修饰符",
			ctx.previous
		)
		
		Modifier.ABSTRACT in modifiers && !isSupportedAbstract -> syntaxError(
			"${name}不支持 'abstract' 修饰符",
			ctx.previous
		)
		
		Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers && !isSupportedFinalOverride -> syntaxError(
			"${name}不支持 \"final override\" 修饰符",
			ctx.previous
		)
		
		Modifier.OVERRIDE in modifiers && !isSupportedOverride -> syntaxError(
			"${name}不支持 'override' 修饰符",
			ctx.previous
		)
	}
}