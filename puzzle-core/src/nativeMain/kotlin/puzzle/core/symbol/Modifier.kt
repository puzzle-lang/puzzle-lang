package puzzle.core.symbol

enum class Modifier(
	val order: Int
) {
	PRIVATE(0),
	PROTECTED(0),
	FILE(0),
	INTERNAL(0),
	MODULE(0),
	PUBLIC(0),
	FINAL(1),
	OPEN(2),
	ABSTRACT(2),
	SEALED(2),
	OVERRIDE(2),
	CONST(3),
	OWNER(3),
	IGNORE(3),
	LATE(3),
	LAZY(3),
	ARGS(3),
	VAR(4),
	VAL(4)
}