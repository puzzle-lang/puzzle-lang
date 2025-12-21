package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.parser.parser.ModifierTarget

enum class ParameterTarget(
	val modifierTarget: ModifierTarget,
	val allowUnnamed: Boolean,
	val allowLambdaType: Boolean,
	val allowWithoutParen: Boolean,
) {
	FUN(
		modifierTarget = ModifierTarget.FUN_PARAMETER,
		allowUnnamed = false,
		allowLambdaType = true,
		allowWithoutParen = false
	),
	CLASS(
		modifierTarget = ModifierTarget.CLASS_PARAMETER,
		allowUnnamed = false,
		allowLambdaType = true,
		allowWithoutParen = true
	),
	ANNOTATION(
		modifierTarget = ModifierTarget.ANNOTATION_PARAMETER,
		allowUnnamed = false,
		allowLambdaType = false,
		allowWithoutParen = true
	),
	ENUM(
		modifierTarget = ModifierTarget.ENUM_PARAMETER,
		allowUnnamed = false,
		allowLambdaType = true,
		allowWithoutParen = true
	),
	STRUCT(
		modifierTarget = ModifierTarget.STRUCT_PARAMETER,
		allowUnnamed = false,
		allowLambdaType = false,
		allowWithoutParen = true
	),
	LAMBDA(
		modifierTarget = ModifierTarget.LAMBDA_PARAMETER,
		allowUnnamed = true,
		allowLambdaType = true,
		allowWithoutParen = false
	),
	CTOR(
		modifierTarget = ModifierTarget.CTOR_PARAMETER,
		allowUnnamed = false,
		allowLambdaType = true,
		allowWithoutParen = false
	)
}