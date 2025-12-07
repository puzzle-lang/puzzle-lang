package puzzle.core.parser.parser.identifier

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlToken
import puzzle.core.lexer.PzlTokenType.*
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor

private val softKeywords = arrayOf(
	PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
	FINAL,
	OPEN, ABSTRACT, SEALED, OVERRIDE,
	CONST, OWNER, IGNORE, LATE, LAZY, ARGS,
	GET, SET,
	TYPE, REIFIED,
	CONTEXT,
	INIT, DELETE,
	PACKAGE, IMPORT
)

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIdentifierName(target: IdentifierNameTarget): String {
	return tryParseIdentifierName(target)
		?: syntaxError(target.notFoundMessage, cursor.current)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun tryParseIdentifierName(target: IdentifierNameTarget): String? {
	if (cursor.match(IDENTIFIER)) {
		val value = cursor.previous.value
		if (value == "_" && !target.isSupportedAnonymity) {
			syntaxError(target.notSupportedAnonymityMessage, cursor.previous)
		}
		return value
	}
	softKeywords.forEach { type ->
		if (cursor.match(type)) {
			return cursor.previous.value
		}
	}
	return null
}

fun PzlToken.isIdentifier(): Boolean {
	return this.type == IDENTIFIER || softKeywords.any { this.type == it }
}

context(cursor: PzlTokenCursor)
fun matchIdentifierName(): Boolean {
	return cursor.match(IDENTIFIER) || softKeywords.any { cursor.match(it) }
}