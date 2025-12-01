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
    OPEN(1),
    ABSTRACT(1),
    SEALED(1),
    OVERRIDE(2),
    CONST(2),
    OWNER(2),
    IGNORE(2),
    LATE(3),
    ARGS(4),
    VAR(5),
    VAL(5)
}