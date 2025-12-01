package puzzle.core.parser.parser.identifier

enum class IdentifierNameTarget(
    val isSupportedAnonymity: Boolean,
    val notFoundMessage: String,
    val notSupportedAnonymityMessage: String = ""
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
}