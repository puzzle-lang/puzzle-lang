package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.ast.declaration.EnumEntry
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.parameter.parameter.parseEnumParameters
import puzzle.core.token.kinds.BracketKind.End.RBRACE
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.SeparatorKind.COMMA
import puzzle.core.token.kinds.SeparatorKind.SEMICOLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseEnumDeclaration(header: DeclarationHeader, start: SourceLocation): EnumDeclaration {
	val name = parseIdentifier(IdentifierTarget.ENUM)
	val parameters = parseEnumParameters()
	cursor.expect(LBRACE, "枚举缺少 '{'")
	val entries = parseEnumEntries()
	val members = if (cursor.previous.kind == RBRACE) {
		emptyList()
	} else {
		buildList {
			while (!cursor.match(RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	}
	val end = cursor.previous.location
	return EnumDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		parameters = parameters,
		entries = entries,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseEnumEntries(): List<EnumEntry> {
	val entries = mutableListOf<EnumEntry>()
	while (!cursor.match(SEMICOLON) && !cursor.match(RBRACE)) {
		entries += parseEnumEntry()
		if (!cursor.check(SEMICOLON) && !cursor.check(RBRACE)) {
			cursor.match(COMMA)
		}
	}
	if (entries.isEmpty()) {
		syntaxError("请至少为枚举设置一个常量", cursor.previous)
	}
	return entries
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseEnumEntry(): EnumEntry {
	val name = parseIdentifier(IdentifierTarget.ENUM_ENTRY)
	val start = name.location
	if (cursor.match(LPAREN)) {
		while (!cursor.match(RPAREN)) {
			cursor.advance()
		}
	}
	val members = mutableListOf<Declaration>()
	if (cursor.match(LBRACE)) {
		while (!cursor.match(RBRACE)) {
			members += parseMemberDeclaration()
		}
	}
	val end = cursor.previous.location
	return EnumEntry(
		name = name,
		members = members,
		location = start span end
	)
}