package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.ast.declaration.EnumEntry
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.parameter.parameter.parseEnumParameters
import puzzle.core.token.SourceLocation
import puzzle.core.token.kinds.BracketKind
import puzzle.core.token.kinds.SeparatorKind
import puzzle.core.token.span

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseEnumDeclaration(header: DeclarationHeader, start: SourceLocation): EnumDeclaration {
	val name = parseIdentifierExpression(IdentifierTarget.ENUM)
	val parameters = parseEnumParameters()
	cursor.expect(BracketKind.Start.LBRACE, "枚举缺少 '{'")
	val entries = parseEnumEntries()
	val members = if (cursor.previous.kind == BracketKind.End.RBRACE) {
		emptyList()
	} else {
		buildList {
			while (!cursor.match(BracketKind.End.RBRACE)) {
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
	while (!cursor.match(SeparatorKind.SEMICOLON) && !cursor.match(BracketKind.End.RBRACE)) {
		entries += parseEnumEntry()
		if (!cursor.check(SeparatorKind.SEMICOLON) && !cursor.check(BracketKind.End.RBRACE)) {
			cursor.match(SeparatorKind.COMMA)
		}
	}
	if (entries.isEmpty()) {
		syntaxError("请至少为枚举设置一个常量", cursor.previous)
	}
	return entries
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseEnumEntry(): EnumEntry {
	val name = parseIdentifierExpression(IdentifierTarget.ENUM_ENTRY)
	val start = name.location
	if (cursor.match(BracketKind.Start.LPAREN)) {
		while (!cursor.match(BracketKind.End.RPAREN)) {
			cursor.advance()
		}
	}
	val members = mutableListOf<Declaration>()
	if (cursor.match(BracketKind.Start.LBRACE)) {
		while (!cursor.match(BracketKind.End.RBRACE)) {
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