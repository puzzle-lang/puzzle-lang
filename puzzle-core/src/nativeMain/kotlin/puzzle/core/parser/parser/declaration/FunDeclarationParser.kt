package puzzle.core.parser.parser.declaration

import puzzle.core.constants.PzlTypes
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.NamedType
import puzzle.core.parser.ast.TypeReference
import puzzle.core.parser.ast.binding.ContextSpec
import puzzle.core.parser.ast.binding.TypeSpec
import puzzle.core.parser.ast.declaration.FunDeclaration
import puzzle.core.parser.matcher.statement.parseStatement
import puzzle.core.parser.parser.binding.parameter.parseFunParameters
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseExtensionAndIdentifierName
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.symbol.Modifier

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseFunDeclaration(
    typeSpec: TypeSpec?,
    contextSpec: ContextSpec?,
    modifiers: List<Modifier>
): FunDeclaration {
    val (extension, name) = parseExtensionAndIdentifierName(IdentifierNameTarget.FUN)
    val parameters = parseFunParameters()
    val returnTypes = mutableListOf<TypeReference>()
    if (cursor.match(PzlTokenType.COLON)) {
        do {
            returnTypes += parseTypeReference(isSupportedLambdaType = true)
        } while (cursor.match(PzlTokenType.COMMA))
    } else {
        returnTypes += TypeReference(PzlTypes.Unit)
    }
    val statements = if (cursor.match(PzlTokenType.LBRACE)) {
        buildList {
            while (!cursor.match(PzlTokenType.RBRACE)) {
                this += parseStatement()
            }
        }
    } else emptyList()
    return FunDeclaration(
        name = name,
        parameters = parameters,
        modifiers = modifiers,
        returnTypes = returnTypes,
        extension = extension,
        typeSpec = typeSpec,
        contextSpec = contextSpec,
        statements = statements
    )
}