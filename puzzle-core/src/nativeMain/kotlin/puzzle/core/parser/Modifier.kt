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
fun getTopLevelAccessModifier(cursor: PzlTokenCursor): Modifier {
	return when {
		cursor.match(PzlTokenType.PRIVATE) -> Modifier.PRIVATE
		cursor.match(PzlTokenType.PROTECTED) -> syntaxError("顶层声明不支持 'protected' 修饰符", cursor.previous)
		cursor.match(PzlTokenType.FILE) -> syntaxError("顶层声明不支持 'file' 修饰符", cursor.previous)
		cursor.match(PzlTokenType.INTERNAL) -> Modifier.INTERNAL
		cursor.match(PzlTokenType.MODULE) -> Modifier.MODULE
		cursor.match(PzlTokenType.PUBLIC) -> Modifier.PUBLIC
		else -> Modifier.PUBLIC
	}
}

context(_: PzlContext)
fun getMemberAccessModifier(cursor: PzlTokenCursor, parentAccess: Modifier, errorMessage: () -> String): Modifier {
	val memberAccess = when {
		cursor.match(PzlTokenType.PRIVATE) -> Modifier.PRIVATE
		cursor.match(PzlTokenType.PROTECTED) -> Modifier.PROTECTED
		cursor.match(PzlTokenType.FILE) -> Modifier.FILE
		cursor.match(PzlTokenType.INTERNAL) -> Modifier.INTERNAL
		cursor.match(PzlTokenType.MODULE) -> Modifier.MODULE
		cursor.match(PzlTokenType.PUBLIC) -> Modifier.PUBLIC
		else -> null
	}
	return if (memberAccess != null) {
		if (!((parentAccess == Modifier.PRIVATE && memberAccess <= Modifier.FILE) || (memberAccess <= parentAccess))) {
			syntaxError(errorMessage(), cursor.previous)
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
fun getClassParameterAccessModifier(cursor: PzlTokenCursor, classAccess: Modifier, errorMessage: () -> String): Modifier? {
	val access = when {
		cursor.match(PzlTokenType.PRIVATE) -> Modifier.PRIVATE
		cursor.match(PzlTokenType.PROTECTED) -> Modifier.PROTECTED
		cursor.match(PzlTokenType.FILE) -> Modifier.FILE
		cursor.match(PzlTokenType.INTERNAL) -> Modifier.INTERNAL
		cursor.match(PzlTokenType.MODULE) -> Modifier.MODULE
		cursor.match(PzlTokenType.PUBLIC) -> Modifier.PUBLIC
		else -> return null
	}
	if (!((classAccess == Modifier.PRIVATE && access <= Modifier.FILE) || (access <= classAccess))) {
		syntaxError(errorMessage(), cursor.previous)
	}
	return access
}

fun getDeclarationModifiers(cursor: PzlTokenCursor): Set<Modifier> {
	val modifiers = mutableSetOf<Modifier>()
	when {
		cursor.match(PzlTokenType.CONST) -> modifiers += Modifier.CONST
		cursor.match(PzlTokenType.OPEN) -> modifiers += Modifier.OPEN
		cursor.match(PzlTokenType.ABSTRACT) -> modifiers += Modifier.ABSTRACT
		cursor.match(PzlTokenType.OVERRIDE) -> modifiers += Modifier.OVERRIDE
		cursor.match(PzlTokenType.FINAL, PzlTokenType.OVERRIDE) -> {
			modifiers += Modifier.FINAL
			modifiers += Modifier.OVERRIDE
		}
	}
	return modifiers
}

context(_: PzlContext)
fun checkSupportedDeclarationModifiers(
	cursor: PzlTokenCursor,
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
			cursor.previous
		)
		
		Modifier.OPEN in modifiers && !isSupportedOpen -> syntaxError(
			"${name}不支持 'open' 修饰符",
			cursor.previous
		)
		
		Modifier.ABSTRACT in modifiers && !isSupportedAbstract -> syntaxError(
			"${name}不支持 'abstract' 修饰符",
			cursor.previous
		)
		
		Modifier.FINAL in modifiers && Modifier.OVERRIDE in modifiers && !isSupportedFinalOverride -> syntaxError(
			"${name}不支持 \"final override\" 修饰符",
			cursor.previous
		)
		
		Modifier.OVERRIDE in modifiers && !isSupportedOverride -> syntaxError(
			"${name}不支持 'override' 修饰符",
			cursor.previous
		)
	}
}