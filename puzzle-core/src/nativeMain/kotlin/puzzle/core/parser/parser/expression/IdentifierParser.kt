package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Identifier
import puzzle.core.token.PzlToken
import puzzle.core.token.kinds.AccessorKind.GET
import puzzle.core.token.kinds.AccessorKind.SET
import puzzle.core.token.kinds.ContextualKind.*
import puzzle.core.token.kinds.IdentifierKind
import puzzle.core.token.kinds.KeywordKind
import puzzle.core.token.kinds.ModifierKind.*
import puzzle.core.token.kinds.NamespaceKind.IMPORT
import puzzle.core.token.kinds.NamespaceKind.PACKAGE

private val softKeywords = arrayOf<KeywordKind>(
	PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
	FINAL,
	OPEN, ABSTRACT, SEALED, OVERRIDE,
	CONST, INNER, IGNORE, LATE, LAZY, ARGS,
	GET, SET,
	TYPE, REIFIED,
	CONTEXT,
	INIT,
	PACKAGE, IMPORT
)

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIdentifier(target: IdentifierTarget): Identifier {
	return tryParseIdentifier(target)
		?: syntaxError("${target.displayName}缺少名称", cursor.current)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun tryParseIdentifier(target: IdentifierTarget): Identifier? {
	return tryParseIdentifierString(target)?.let {
		Identifier(it, cursor.previous.location)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIdentifierString(target: IdentifierTarget): String {
	return tryParseIdentifierString(target)
		?: syntaxError("${target.displayName}缺少名称", cursor.current)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun tryParseIdentifierString(target: IdentifierTarget): String? {
	if (cursor.match<IdentifierKind>()) {
		val value = cursor.previous.value
		if (value == "_" && !target.allowAnonymousBinding) {
			syntaxError("${target.displayName}不支持匿名绑定", cursor.previous)
		}
		return value
	}
	softKeywords.forEach {
		if (cursor.match(it)) {
			return cursor.previous.value
		}
	}
	return null
}

fun PzlToken.isIdentifier(): Boolean {
	return this.kind is IdentifierKind || softKeywords.any { this.kind == it }
}

context(cursor: PzlTokenCursor)
fun matchIdentifier(): Boolean {
	return cursor.match<IdentifierKind>() || softKeywords.any { cursor.match(it) }
}

enum class IdentifierTarget(
	val displayName: String,
	val allowAnonymousBinding: Boolean,
) {
	FUN(
		displayName = "函数声明",
		allowAnonymousBinding = false,
	),
	PROPERTY(
		displayName = "属性声明",
		allowAnonymousBinding = false,
	),
	CLASS(
		displayName = "类声明",
		allowAnonymousBinding = false,
	),
	TRAIT(
		displayName = "特征声明",
		allowAnonymousBinding = false,
	),
	STRUCT(
		displayName = "结构体声明",
		allowAnonymousBinding = false,
	),
	OBJECT(
		displayName = "单例类声明",
		allowAnonymousBinding = false
	),
	MEMBER_OBJECT(
		displayName = "成员单例类声明",
		allowAnonymousBinding = true
	),
	ANNOTATION(
		displayName = "注解声明",
		allowAnonymousBinding = false
	),
	ENUM(
		displayName = "枚举声明",
		allowAnonymousBinding = false
	),
	ENUM_ENTRY(
		displayName = "枚举成员声明",
		allowAnonymousBinding = false,
	),
	CTOR(
		displayName = "构造函数声明",
		allowAnonymousBinding = false
	),
	CONTEXT_RECEIVER(
		displayName = "上下文型参",
		allowAnonymousBinding = true
	),
	TYPE_PARAMETER(
		displayName = "泛型型参",
		allowAnonymousBinding = false
	),
	PARAMETER(
		displayName = "型参",
		allowAnonymousBinding = false,
	),
	VARIABLE(
		displayName = "变量",
		allowAnonymousBinding = false
	),
	TYPE_REFERENCE(
		displayName = "类型",
		allowAnonymousBinding = false
	),
	SUFFIX_UNARY(
		displayName = "一元运算符前",
		allowAnonymousBinding = false
	),
	PREFIX_UNARY(
		displayName = "一元运算符后",
		allowAnonymousBinding = false,
	),
	PACKAGE(
		displayName = "包",
		allowAnonymousBinding = false,
	),
	IMPORT(
		displayName = "导入",
		allowAnonymousBinding = false,
	),
	IMPORT_AS(
		displayName = "导入别名",
		allowAnonymousBinding = false,
	),
	ACCESS_OPERATOR(
		displayName = "成员访问",
		allowAnonymousBinding = false,
	),
	ARGUMENT(
		displayName = "实参",
		allowAnonymousBinding = false,
	),
	TYPE_ARGUMENT(
		displayName = "泛型实参",
		allowAnonymousBinding = false
	),
	LABEL(
		displayName = "标签",
		allowAnonymousBinding = false
	),
	FOR_VARIABLE(
		displayName = "for 语句循环变量",
		allowAnonymousBinding = true,
	),
}