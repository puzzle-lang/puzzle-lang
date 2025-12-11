package puzzle.core.parser.parser.identifier

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.token.AccessorKind.GET
import puzzle.core.token.AccessorKind.SET
import puzzle.core.token.ContextualKind.*
import puzzle.core.token.IdentifierKind
import puzzle.core.token.KeywordKind
import puzzle.core.token.ModifierKind.*
import puzzle.core.token.NamespaceKind.IMPORT
import puzzle.core.token.NamespaceKind.PACKAGE
import puzzle.core.token.PzlToken

private val softKeywords = arrayOf<KeywordKind>(
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
	if (cursor.match<IdentifierKind>()) {
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

fun PzlToken.isIdentifierName(): Boolean {
	return this.kind is IdentifierKind || softKeywords.any { this.kind == it }
}

context(cursor: PzlTokenCursor)
fun matchIdentifierName(): Boolean {
	return cursor.match<IdentifierKind>() || softKeywords.any { cursor.match(it) }
}