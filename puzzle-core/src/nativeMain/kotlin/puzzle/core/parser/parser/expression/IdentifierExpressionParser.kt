package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.IdentifierExpression
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
	INIT, DELETE,
	PACKAGE, IMPORT
)

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIdentifierExpression(target: IdentifierTarget): IdentifierExpression {
	return tryParseIdentifierExpression(target)
		?: syntaxError(target.notFoundMessage, cursor.current)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun tryParseIdentifierExpression(target: IdentifierTarget): IdentifierExpression? {
	return tryParseIdentifierString(target)?.let {
		IdentifierExpression(it, cursor.previous.location)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIdentifierString(target: IdentifierTarget): String {
	return tryParseIdentifierString(target)
		?: syntaxError(target.notFoundMessage, cursor.current)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun tryParseIdentifierString(target: IdentifierTarget): String? {
	if (cursor.match<IdentifierKind>()) {
		val value = cursor.previous.value
		if (value == "_" && !target.isSupportedAnonymity) {
			syntaxError(target.notSupportedAnonymityMessage, cursor.previous)
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
	val isSupportedAnonymity: Boolean,
	val notFoundMessage: String,
	val notSupportedAnonymityMessage: String = "",
) {
	FUN(
		isSupportedAnonymity = false,
		notFoundMessage = "函数缺少名称",
		notSupportedAnonymityMessage = "函数不支持匿名"
	),
	PROPERTY(
		isSupportedAnonymity = false,
		notFoundMessage = "属性缺少名称",
		notSupportedAnonymityMessage = "属性不支持匿名"
	),
	CLASS(
		isSupportedAnonymity = false,
		notFoundMessage = "类缺少名称",
		notSupportedAnonymityMessage = "类不支持匿名"
	),
	TRAIT(
		isSupportedAnonymity = false,
		notFoundMessage = "特征缺少名称",
		notSupportedAnonymityMessage = "特征不支持匿名"
	),
	STRUCT(
		isSupportedAnonymity = false,
		notFoundMessage = "结构体缺少名称",
		notSupportedAnonymityMessage = "结构体不支持匿名"
	),
	UNIQUE(
		isSupportedAnonymity = false,
		notFoundMessage = "单例类缺少名称",
		notSupportedAnonymityMessage = "单例类不支持匿名"
	),
	MEMBER_UNIQUE(
		isSupportedAnonymity = true,
		notFoundMessage = "单例类缺少名称"
	),
	ANNOTATION(
		isSupportedAnonymity = false,
		notFoundMessage = "注解缺少名称",
		notSupportedAnonymityMessage = "注解不支持匿名"
	),
	ENUM(
		isSupportedAnonymity = false,
		notFoundMessage = "枚举缺少名称",
		notSupportedAnonymityMessage = "枚举不支持匿名"
	),
	ENUM_ENTRY(
		isSupportedAnonymity = false,
		notFoundMessage = "枚举常量缺少名称",
		notSupportedAnonymityMessage = "枚举常量不支持匿名"
	),
	CONTEXT_RECEIVER(
		isSupportedAnonymity = true,
		notFoundMessage = "上下文参数缺少名称",
	),
	TYPE_PARAMETER(
		isSupportedAnonymity = false,
		notFoundMessage = "泛型参数缺少名称",
		notSupportedAnonymityMessage = "泛型参数不支持匿名"
	),
	PARAMETER(
		isSupportedAnonymity = false,
		notFoundMessage = "参数缺少名称",
		notSupportedAnonymityMessage = "参数不支持匿名"
	),
	VARIABLE(
		isSupportedAnonymity = false,
		notFoundMessage = "变量缺少名称",
		notSupportedAnonymityMessage = "变量不支持匿名"
	),
	TYPE_REFERENCE(
		isSupportedAnonymity = false,
		notFoundMessage = "无法识别标识符",
		notSupportedAnonymityMessage = "不支持匿名标识符"
	),
	SUFFIX_UNARY(
		isSupportedAnonymity = false,
		notFoundMessage = "一元运算符前必须跟标识符",
		notSupportedAnonymityMessage = "一元运算符前不允许使用匿名标识符"
	),
	PREFIX_UNARY(
		isSupportedAnonymity = false,
		notFoundMessage = "一元运算符后必须跟标识符"
	),
	PACKAGE(
		isSupportedAnonymity = false,
		notFoundMessage = "package 后缺少包名",
		notSupportedAnonymityMessage = "package 后不允许使用匿名标识符"
	),
	PACKAGE_DOT(
		isSupportedAnonymity = false,
		notFoundMessage = "'.' 后缺少标识符",
		notSupportedAnonymityMessage = "'.' 后不允许使用匿名标识符"
	),
	IMPORT(
		isSupportedAnonymity = false,
		notFoundMessage = "import 后缺少包名",
		notSupportedAnonymityMessage = "import 后不允许使用匿名标识符"
	),
	IMPORT_AS(
		isSupportedAnonymity = false,
		notFoundMessage = "as 后缺少名称",
		notSupportedAnonymityMessage = "别名不支持匿名"
	),
	ACCESS_OPERATOR(
		isSupportedAnonymity = false,
		notFoundMessage = "访问操作符后必须跟标识符",
		notSupportedAnonymityMessage = "访问操作符后不支持匿名标识符"
	),
	ARGUMENT(
		isSupportedAnonymity = false,
		notFoundMessage = "参数名称必须为标识符",
		notSupportedAnonymityMessage = "参数不支持匿名"
	),
	TYPE_ARGUMENT(
		isSupportedAnonymity = false,
		notFoundMessage = "泛型参数名称必须为标识符",
		notSupportedAnonymityMessage = "泛型参数不支持匿名"
	),
	LABEL(
		isSupportedAnonymity = false,
		notFoundMessage = "标签必须为标识符",
		notSupportedAnonymityMessage = "标签不支持匿名"
	)
}