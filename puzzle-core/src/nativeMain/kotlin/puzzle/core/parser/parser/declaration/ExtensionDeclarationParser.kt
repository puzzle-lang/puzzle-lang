package puzzle.core.parser.parser.declaration

import puzzle.core.PzlContext
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.ast.declaration.SuperTrait
import puzzle.core.parser.matcher.declaration.member.parseMemberDeclaration
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.parser.node.TypeReferenceParser
import puzzle.core.symbol.Modifier

class ExtensionDeclarationParser private constructor(
    private val cursor: PzlTokenCursor
) : PzlParser {

    companion object : PzlParserProvider<ExtensionDeclarationParser>(::ExtensionDeclarationParser)

    context(_: PzlContext)
    fun parse(
	    typeSpec: TypeSpec?,
	    contextSpec: ContextSpec?,
	    modifiers: List<Modifier>
    ): ExtensionDeclaration {
        val extendedType = TypeReferenceParser.of(cursor).parse()
        val superTraits = parseSuperTypes(cursor, isSupportedClass = false)
            .filterIsInstance<SuperTrait>()
        val members = if (cursor.match(PzlTokenType.LBRACE)) {
            buildList {
                while (!cursor.match(PzlTokenType.RBRACE)) {
                    this += parseMemberDeclaration(cursor)
                }
            }
        } else emptyList()
        return ExtensionDeclaration(
            extendedType = extendedType,
            modifiers = modifiers,
            superTraits = superTraits,
            typeSpec = typeSpec,
            contextSpec = contextSpec,
            members = members,
        )
    }
}