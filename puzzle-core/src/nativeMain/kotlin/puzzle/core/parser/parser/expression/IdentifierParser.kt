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
		?: syntaxError(target.missingIdentifierMessage, cursor.current)
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
		?: syntaxError(target.missingIdentifierMessage, cursor.current)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun tryParseIdentifierString(target: IdentifierTarget): String? {
	if (cursor.match<IdentifierKind>()) {
		val value = cursor.previous.value
		if (value == "_" && !target.allowsAnonymousBinding) {
			syntaxError(target.anonymousNotAllowedMessage, cursor.previous)
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
	val allowsAnonymousBinding: Boolean,
	val missingIdentifierMessage: String,
	val anonymousNotAllowedMessage: String = "",
) {
	FUN(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "函数声明缺少名称",
		anonymousNotAllowedMessage = "函数不支持匿名声明"
	),
	PROPERTY(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "属性声明缺少名称",
		anonymousNotAllowedMessage = "属性不支持匿名声明"
	),
	CLASS(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "类声明缺少名称",
		anonymousNotAllowedMessage = "类不支持匿名声明"
	),
	TRAIT(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "特征声明缺少名称",
		anonymousNotAllowedMessage = "特征不支持匿名声明"
	),
	STRUCT(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "结构体声明缺少名称",
		anonymousNotAllowedMessage = "结构体不支持匿名声明"
	),
	OBJECT(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "单例声明缺少名称",
		anonymousNotAllowedMessage = "单例不支持匿名声明"
	),
	MEMBER_OBJECT(
		allowsAnonymousBinding = true,
		missingIdentifierMessage = "成员单例缺少名称"
	),
	ANNOTATION(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "注解声明缺少名称",
		anonymousNotAllowedMessage = "注解不支持匿名声明"
	),
	ENUM(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "枚举声明缺少名称",
		anonymousNotAllowedMessage = "枚举不支持匿名声明"
	),
	ENUM_ENTRY(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "枚举成员缺少名称",
		anonymousNotAllowedMessage = "枚举成员不支持匿名"
	),
	CONTEXT_RECEIVER(
		allowsAnonymousBinding = true,
		missingIdentifierMessage = "上下文参数缺少名称"
	),
	TYPE_PARAMETER(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "泛型参数缺少名称",
		anonymousNotAllowedMessage = "泛型参数不支持匿名"
	),
	PARAMETER(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "参数缺少名称",
		anonymousNotAllowedMessage = "参数不支持匿名"
	),
	VARIABLE(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "变量声明缺少名称",
		anonymousNotAllowedMessage = "变量不支持匿名"
	),
	TYPE_REFERENCE(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "缺少类型名称",
		anonymousNotAllowedMessage = "类型位置不支持匿名标识"
	),
	SUFFIX_UNARY(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "一元运算符前缺少操作数",
		anonymousNotAllowedMessage = "一元运算符不支持匿名操作数"
	),
	PREFIX_UNARY(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "一元运算符后缺少操作数"
	),
	PACKAGE(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "package 声明缺少包名",
		anonymousNotAllowedMessage = "package 声明不支持匿名"
	),
	PACKAGE_DOT(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "包路径中缺少名称",
		anonymousNotAllowedMessage = "包路径不支持匿名"
	),
	IMPORT(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "import 语句缺少目标名称",
		anonymousNotAllowedMessage = "import 不支持匿名"
	),
	IMPORT_AS(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "import 别名缺少名称",
		anonymousNotAllowedMessage = "import 别名不支持匿名"
	),
	ACCESS_OPERATOR(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "成员访问缺少名称",
		anonymousNotAllowedMessage = "成员访问不支持匿名"
	),
	ARGUMENT(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "参数名称必须为有效标识",
		anonymousNotAllowedMessage = "参数名称不支持匿名"
	),
	TYPE_ARGUMENT(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "泛型参数名称必须为有效标识",
		anonymousNotAllowedMessage = "泛型参数名称不支持匿名"
	),
	LABEL(
		allowsAnonymousBinding = false,
		missingIdentifierMessage = "标签缺少名称",
		anonymousNotAllowedMessage = "标签不支持匿名"
	),
	FOR_VARIABLE(
		allowsAnonymousBinding = true,
		missingIdentifierMessage = "for 语句缺少循环变量"
	)
}