package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.parser.parser.ModifierTarget

enum class ParameterTarget(
	val label: String,
	val modifierTarget: ModifierTarget,
	val allowLambdaType: Boolean,
	val allowWithoutParen: Boolean,
	val allowVarargQuantifier: Boolean,
) {
	FUN(
		label = "函数",
		modifierTarget = ModifierTarget.FUN_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = false,
		allowVarargQuantifier = true
	),
	CLASS(
		label = "类",
		modifierTarget = ModifierTarget.CLASS_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = true,
		allowVarargQuantifier = true
	),
	OBJECT(
		label = "单例对象",
		modifierTarget = ModifierTarget.OBJECT_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = true,
		allowVarargQuantifier = true
	),
	ANNOTATION(
		label = "注解",
		modifierTarget = ModifierTarget.ANNOTATION_PARAMETER,
		allowLambdaType = false,
		allowWithoutParen = true,
		allowVarargQuantifier = true
	),
	ENUM(
		label = "枚举",
		modifierTarget = ModifierTarget.ENUM_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = true,
		allowVarargQuantifier = true
	),
	STRUCT(
		label = "结构体",
		modifierTarget = ModifierTarget.STRUCT_PARAMETER,
		allowLambdaType = false,
		allowWithoutParen = true,
		allowVarargQuantifier = false
	),
	CTOR(
		label = "次构造函数",
		modifierTarget = ModifierTarget.CTOR_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = false,
		allowVarargQuantifier = true
	)
}