package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.ast.declaration.EnumEntry
import puzzle.core.parser.matcher.declaration.parseMemberDeclaration
import puzzle.core.parser.parser.binding.parameter.parseEnumParameters
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseEnumDeclaration(
    typeSpec: TypeSpec?,
    contextSpec: ContextSpec?,
    modifiers: List<Modifier>
): EnumDeclaration {
    val name = parseIdentifierName(IdentifierNameTarget.ENUM)
    val parameters = parseEnumParameters()
    cursor.expect(PzlTokenType.LBRACE, "枚举缺少 '{'")
    val entries = parseEnumEntries()
    val members = if (cursor.previous.type == PzlTokenType.RBRACE) {
        emptyList()
    } else {
        buildList {
            while (!cursor.match(PzlTokenType.RBRACE)) {
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
        members = members,
    )
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseEnumEntries(): List<EnumEntry> {
    val entries = mutableListOf<EnumEntry>()
    while (!cursor.match(PzlTokenType.SEMICOLON) && !cursor.match(PzlTokenType.RBRACE)) {
        entries += parseEnumEntry()
        if (!cursor.check(PzlTokenType.SEMICOLON) && !cursor.check(PzlTokenType.RBRACE)) {
            cursor.match(PzlTokenType.COMMA)
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
    if (cursor.match(PzlTokenType.LPAREN)) {
        while (!cursor.match(PzlTokenType.RPAREN)) {
            cursor.advance()
        }
    }
    val members = mutableListOf<Declaration>()
    if (cursor.match(PzlTokenType.LBRACE)) {
        while (!cursor.match(PzlTokenType.RBRACE)) {
            members += parseMemberDeclaration()
        }
    }
    return EnumEntry(
        name = name,
        members = members
    )
}