package puzzle.core.parser.parser.parameter.parameter

import puzzle.core.parser.parser.ModifierTarget

enum class ParameterTarget(
	val modifierTarget: ModifierTarget,
	val allowLambdaType: Boolean,
	val allowWithoutParen: Boolean,
) {
	FUN(
		modifierTarget = ModifierTarget.FUN_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = false
	),
	CLASS(
		modifierTarget = ModifierTarget.CLASS_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = true
	),
	OBJECT(
		modifierTarget = ModifierTarget.OBJECT_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = true
	),
	ANNOTATION(
		modifierTarget = ModifierTarget.ANNOTATION_PARAMETER,
		allowLambdaType = false,
		allowWithoutParen = true
	),
	ENUM(
		modifierTarget = ModifierTarget.ENUM_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = true
	),
	STRUCT(
		modifierTarget = ModifierTarget.STRUCT_PARAMETER,
		allowLambdaType = false,
		allowWithoutParen = true
	),
	CTOR(
		modifierTarget = ModifierTarget.CTOR_PARAMETER,
		allowLambdaType = true,
		allowWithoutParen = false
	)
}