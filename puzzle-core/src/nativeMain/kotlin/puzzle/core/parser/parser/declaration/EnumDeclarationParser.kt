package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.parser.ast.declaration.EnumDeclaration
import puzzle.core.parser.ast.declaration.EnumEntry
import puzzle.core.parser.matcher.declaration.member.parseMemberDeclaration
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.binding.parameter.parseEnumParameters
import puzzle.core.parser.parser.identifier.IdentifierNameParser
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.symbol.Modifier

class EnumDeclarationParser private constructor(
    private val cursor: PzlTokenCursor
) : PzlParser {

    companion object : PzlParserProvider<EnumDeclarationParser>(::EnumDeclarationParser)

    context(_: PzlContext)
    fun parse(
	    typeSpec: TypeSpec?,
	    contextSpec: ContextSpec?,
	    modifiers: List<Modifier>
    ): EnumDeclaration {
        val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.ENUM)
        val parameters = parseEnumParameters(cursor)
        cursor.expect(PzlTokenType.LBRACE, "枚举缺少 '{'")
        val entries = parseEnumEntries()
        val members = if (cursor.previous.type == PzlTokenType.RBRACE) {
            emptyList()
        } else {
            buildList {
                while (!cursor.match(PzlTokenType.RBRACE)) {
                    this += parseMemberDeclaration(cursor)
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

    context(_: PzlContext)
    private fun parseEnumEntries(): List<EnumEntry> {
        val entries = mutableListOf<EnumEntry>()
        while (!cursor.match(PzlTokenType.SEMICOLON) && !cursor.match(PzlTokenType.RBRACE)) {
            entries += EnumEntryParser.of(cursor).parse()
            if (!cursor.check(PzlTokenType.SEMICOLON) && !cursor.check(PzlTokenType.RBRACE)) {
                cursor.match(PzlTokenType.COMMA)
            }
        }
        if (entries.isEmpty()) {
            syntaxError("请至少为枚举设置一个常量", cursor.previous)
        }
        return entries
    }
}

private class EnumEntryParser private constructor(
    private val cursor: PzlTokenCursor
) : PzlParser {

    companion object : PzlParserProvider<EnumEntryParser>(::EnumEntryParser)

    context(_: PzlContext)
    fun parse(): EnumEntry {
        val name = IdentifierNameParser.of(cursor).parse(IdentifierNameTarget.ENUM_ENTRY)
        if (cursor.match(PzlTokenType.LPAREN)) {
            while (!cursor.match(PzlTokenType.RPAREN)) {
                cursor.advance()
            }
        }
        val members = mutableListOf<Declaration>()
        if (cursor.match(PzlTokenType.LBRACE)) {
            while (!cursor.match(PzlTokenType.RBRACE)) {
                members += parseMemberDeclaration(cursor)
            }
        }
        return EnumEntry(
            name = name,
            members = members
        )
    }
}