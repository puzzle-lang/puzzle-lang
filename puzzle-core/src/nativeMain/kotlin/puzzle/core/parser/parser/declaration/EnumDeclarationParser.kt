package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.AnnotationCall
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.ast.declaration.EnumEntry
import puzzle.core.parser.ast.parameter.ContextSpec
import puzzle.core.parser.ast.parameter.TypeSpec
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parameter.parameter.parseEnumParameters
import puzzle.core.token.BracketKind
import puzzle.core.token.ModifierKind
import puzzle.core.token.SeparatorKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseEnumDeclaration(
	typeSpec: TypeSpec?,
	contextSpec: ContextSpec?,
	modifiers: List<ModifierKind>,
	annotationCalls: List<AnnotationCall>,
): EnumDeclaration {
	val name = parseIdentifierName(IdentifierNameTarget.ENUM)
	val parameters = parseEnumParameters()
	cursor.expect(BracketKind.LBRACE, "枚举缺少 '{'")
	val entries = parseEnumEntries()
	val members = if (cursor.previous.kind == BracketKind.RBRACE) {
		emptyList()
	} else {
		buildList {
			while (!cursor.match(BracketKind.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	}
	return EnumDeclaration(
		name = name,
		modifiers = modifiers,
		parameters = parameters,
		entries = entries,
		typeSpec = typeSpec,
		contextSpec = contextSpec,
		annotationCalls = annotationCalls,
		members = members,
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseEnumEntries(): List<EnumEntry> {
	val entries = mutableListOf<EnumEntry>()
	while (!cursor.match(SeparatorKind.SEMICOLON) && !cursor.match(BracketKind.RBRACE)) {
		entries += parseEnumEntry()
		if (!cursor.check(SeparatorKind.SEMICOLON) && !cursor.check(BracketKind.RBRACE)) {
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
	val name = parseIdentifierName(IdentifierNameTarget.ENUM_ENTRY)
	if (cursor.match(BracketKind.LPAREN)) {
		while (!cursor.match(BracketKind.RPAREN)) {
			cursor.advance()
		}
	}
	val members = mutableListOf<Declaration>()
	if (cursor.match(BracketKind.LBRACE)) {
		while (!cursor.match(BracketKind.RBRACE)) {
			members += parseMemberDeclaration()
		}
	}
	return EnumEntry(
		name = name,
		members = members
	)
}