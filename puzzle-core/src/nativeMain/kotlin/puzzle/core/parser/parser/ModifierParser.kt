package puzzle.core.parser.parser

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Modifier
import puzzle.core.token.kinds.ModifierKind
import puzzle.core.token.kinds.ModifierKind.*
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseModifiers(): List<Modifier> {
	val modifiers = buildList {
		while (true) {
			val modifier = parseModifier() ?: break
			val kind = modifier.kind
			if (kind == VAR || kind == VAR) break
			this += modifier
		}
	}
	checkModifierOrder(modifiers)
	return modifiers
}

context(cursor: PzlTokenCursor)
private fun parseModifier(): Modifier? {
	ModifierKind.kinds.fastForEach { kind ->
		if (cursor.current.kind == kind && cursor.nextOrNull?.kind != COLON) {
			val token = cursor.advance()
			return Modifier(kind, token.location)
		}
	}
	return null
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun checkModifierOrder(modifiers: List<Modifier>) {
	if (modifiers.isEmpty()) return
	var last: Modifier? = null
	var lastOrder = -1
	modifiers.forEachIndexed { index, current ->
		when {
			current.kind.order > lastOrder -> {
				lastOrder = current.kind.order
				last = current
			}
			
			current.kind.order < lastOrder -> syntaxError(
				message = "不规范的修饰符顺序，'${current.kind.value}' 需要在 '${last!!.kind.value}' 前面",
				token = cursor.offset(offset = -modifiers.size + index)
			)
			
			else -> syntaxError(
				message = "'${last!!.kind.value}' 和 '${current.kind.value}' 修饰符不允许同时使用",
				token = cursor.offset(offset = -modifiers.size + index)
			)
		}
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun List<Modifier>.check(target: ModifierTarget) {
	this.forEachIndexed { index, modifier ->
		if (modifier.kind !in target.supportedModifiers) {
			syntaxError(
				message = "${target.displayName}不支持 '${modifier.kind.value}' 修饰符",
				token = cursor.offset(offset = -this.size + index - 1)
			)
		}
	}
	if (this.all { it.kind != FINAL }) return
	if (this.all { it.kind != OVERRIDE }) {
		syntaxError(
			"‘final’ 修饰符必须配合 'override' 修饰符使用",
			cursor.offset(offset = -this.size + this.indexOfFirst { it.kind == FINAL } - 1)
		)
	}
}

enum class ModifierTarget(
	val displayName: String,
	val supportedModifiers: Set<ModifierKind>,
) {
	FUN(
		displayName = "函数",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST)
	),
	PROPERTY(
		displayName = "属性",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST, LATE, LAZY, VAR, VAL)
	),
	CLASS(
		displayName = "类",
		supportedModifiers = TopLevelAccessModifiers + setOf(OPEN, ABSTRACT, SEALED)
	),
	OBJECT(
		displayName = "单例类",
		supportedModifiers = TopLevelAccessModifiers
	),
	TRAIT(
		displayName = "特征",
		supportedModifiers = TopLevelAccessModifiers + setOf(SEALED)
	),
	STRUCT(
		displayName = "结构体",
		supportedModifiers = TopLevelAccessModifiers
	),
	ENUM(
		displayName = "枚举",
		supportedModifiers = TopLevelAccessModifiers
	),
	ANNOTATION(
		displayName = "注解",
		supportedModifiers = TopLevelAccessModifiers
	),
	EXTENSION(
		displayName = "扩展",
		supportedModifiers = TopLevelAccessModifiers
	),
	MEMBER_FUN(
		displayName = "成员函数",
		supportedModifiers = MemberAccessModifiers + setOf(FINAL, OVERRIDE, OPEN, ABSTRACT)
	),
	MEMBER_CTOR(
		displayName = "构造函数",
		supportedModifiers = TopLevelAccessModifiers + setOf(FILE)
	),
	MEMBER_PROPERTY(
		displayName = "属性",
		supportedModifiers = TopLevelAccessModifiers + setOf(CONST, LATE, LAZY, VAR, VAL)
	),
	MEMBER_CLASS(
		displayName = "成员类",
		supportedModifiers = MemberAccessModifiers + setOf(INNER, OPEN, ABSTRACT)
	),
	MEMBER_OBJECT(
		displayName = "成员单例类",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_TRAIT(
		displayName = "成员特征",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_STRUCT(
		displayName = "成员结构体",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_ENUM(
		displayName = "成员枚举",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_ANNOTATION(
		displayName = "成员注解",
		supportedModifiers = MemberAccessModifiers
	),
	MEMBER_EXTENSION(
		displayName = "成员扩展",
		supportedModifiers = MemberAccessModifiers
	),
	FUN_PARAMETER(
		displayName = "函数参数",
		supportedModifiers = setOf(VAR, ARGS)
	),
	CLASS_PARAMETER(
		displayName = "类参数",
		supportedModifiers = MemberAccessModifiers + setOf(OPEN, ABSTRACT, VAR, VAL, ARGS),
	),
	STRUCT_PARAMETER(
		displayName = "结构体参数",
		supportedModifiers = MemberAccessModifiers + setOf(VAR, VAL, IGNORE)
	),
	ENUM_PARAMETER(
		displayName = "枚举参数",
		supportedModifiers = MemberAccessModifiers + setOf(VAR, VAL, ARGS)
	),
	ANNOTATION_PARAMETER(
		displayName = "注解参数",
		supportedModifiers = setOf(VAL)
	),
	LAMBDA_PARAMETER(
		displayName = "lambda 参数",
		supportedModifiers = emptySet()
	),
}

private val MemberAccessModifiers = setOf<ModifierKind>(PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC)

private val TopLevelAccessModifiers = setOf<ModifierKind>(PRIVATE, INTERNAL, MODULE, PUBLIC)