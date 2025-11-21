package puzzle.core.parser

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.Modifier.*

enum class Modifier {
	PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
	VAR, VAL,
	OPEN, ABSTRACT, OVERRIDE, FINAL, CONST, OWNER,
	IGNORE
}

private val accessModifiers = setOf(
	PRIVATE,
	PROTECTED,
	FILE,
	INTERNAL,
	MODULE,
	PUBLIC
)

val Set<Modifier>.access: Modifier
	get() = accessModifiers.find { it in this }
		?: error("未知的修饰符")

val Set<Modifier>.isOpen: Boolean
	get() = OPEN in this

val Set<Modifier>.isAbstract: Boolean
	get() = ABSTRACT in this

val Set<Modifier>.isOwner: Boolean
	get() = OWNER in this

context(_: PzlContext)
fun getTopLevelAccessModifier(cursor: PzlTokenCursor): Modifier {
	return when {
		cursor.match(PzlTokenType.PRIVATE) -> PRIVATE
		cursor.match(PzlTokenType.PROTECTED) -> syntaxError("顶层声明不支持 'protected' 修饰符", cursor.previous)
		cursor.match(PzlTokenType.FILE) -> syntaxError("顶层声明不支持 'file' 修饰符", cursor.previous)
		cursor.match(PzlTokenType.INTERNAL) -> INTERNAL
		cursor.match(PzlTokenType.MODULE) -> MODULE
		cursor.match(PzlTokenType.PUBLIC) -> PUBLIC
		else -> PUBLIC
	}
}

context(_: PzlContext)
fun getMemberAccessModifier(cursor: PzlTokenCursor, parentAccess: Modifier, errorMessage: () -> String): Modifier {
	val memberAccess = when {
		cursor.match(PzlTokenType.PRIVATE) -> PRIVATE
		cursor.match(PzlTokenType.PROTECTED) -> PROTECTED
		cursor.match(PzlTokenType.FILE) -> FILE
		cursor.match(PzlTokenType.INTERNAL) -> INTERNAL
		cursor.match(PzlTokenType.MODULE) -> MODULE
		cursor.match(PzlTokenType.PUBLIC) -> PUBLIC
		else -> null
	}
	return if (memberAccess != null) {
		if (!((parentAccess == PRIVATE && memberAccess <= FILE) || (memberAccess <= parentAccess))) {
			syntaxError(errorMessage(), cursor.previous)
		}
		memberAccess
	} else {
		getDefaultMemberAccessModifier(parentAccess)
	}
}

fun getDefaultMemberAccessModifier(parentAccess: Modifier): Modifier {
	return when (parentAccess) {
		PRIVATE, FILE -> FILE
		INTERNAL -> INTERNAL
		MODULE -> MODULE
		PUBLIC -> PUBLIC
		else -> error("不支持的访问修饰符")
	}
}

context(_: PzlContext)
fun getClassParameterAccessModifier(cursor: PzlTokenCursor, classAccess: Modifier, errorMessage: () -> String): Modifier? {
	val access = when {
		cursor.match(PzlTokenType.PRIVATE) -> PRIVATE
		cursor.match(PzlTokenType.PROTECTED) -> PROTECTED
		cursor.match(PzlTokenType.FILE) -> FILE
		cursor.match(PzlTokenType.INTERNAL) -> INTERNAL
		cursor.match(PzlTokenType.MODULE) -> MODULE
		cursor.match(PzlTokenType.PUBLIC) -> PUBLIC
		else -> return null
	}
	if (!((classAccess == PRIVATE && access <= FILE) || (access <= classAccess))) {
		syntaxError(errorMessage(), cursor.previous)
	}
	return access
}

fun getDeclarationModifiers(
	cursor: PzlTokenCursor
): Set<Modifier> = when {
	cursor.match(PzlTokenType.CONST) -> setOf(CONST)
	cursor.match(PzlTokenType.OWNER) -> setOf(OWNER)
	cursor.match(PzlTokenType.OPEN) -> setOf(OPEN)
	cursor.match(PzlTokenType.ABSTRACT) -> setOf(ABSTRACT)
	cursor.match(PzlTokenType.OVERRIDE) -> setOf(OVERRIDE)
	cursor.match(PzlTokenType.FINAL, PzlTokenType.OVERRIDE) -> setOf(FINAL, OVERRIDE)
	else -> emptySet()
}

enum class DeclarationModifierType {
	CONST,
	OWNER,
	OPEN,
	ABSTRACT,
	OVERRIDE,
	FINAL_OVERRIDE;
	
	companion object {
		
		val topFunTypes = setOf(CONST)
		
		val topClassTypes = setOf(OPEN, ABSTRACT)
		
		
		val memberFunTypes = setOf(ABSTRACT, OVERRIDE, FINAL_OVERRIDE, OPEN)
		
		val enumEntryFunTypes = setOf(OVERRIDE, FINAL_OVERRIDE)
		
		val memberClassTypes = topClassTypes + setOf(OWNER)
	}
}

context(_: PzlContext)
fun checkSupportedDeclarationModifiers(
	cursor: PzlTokenCursor,
	name: String,
	modifiers: Set<Modifier>,
	supportedTypes: Set<DeclarationModifierType> = emptySet()
) {
	when {
		CONST in modifiers && DeclarationModifierType.CONST !in supportedTypes -> {
			syntaxError("${name}不支持 'const' 修饰符", cursor.offset(offset = -2))
		}
		
		OWNER in modifiers && DeclarationModifierType.OWNER !in supportedTypes -> {
			syntaxError("${name}不支持 'owner' 修饰符", cursor.offset(offset = -2))
		}
		
		OPEN in modifiers && DeclarationModifierType.OPEN !in supportedTypes -> {
			syntaxError("${name}不支持 'open' 修饰符", cursor.offset(offset = -2))
		}
		
		ABSTRACT in modifiers && DeclarationModifierType.ABSTRACT !in supportedTypes -> {
			syntaxError("${name}不支持 'abstract' 修饰符", cursor.offset(offset = -2))
		}
		
		FINAL in modifiers && OVERRIDE in modifiers && DeclarationModifierType.FINAL_OVERRIDE !in supportedTypes -> {
			syntaxError("${name}不支持 \"final override\" 修饰符", cursor.offset(offset = -3))
		}
		
		OVERRIDE in modifiers && DeclarationModifierType.OVERRIDE !in supportedTypes -> {
			syntaxError("${name}不支持 'override' 修饰符", cursor.offset(offset = -2))
		}
	}
}