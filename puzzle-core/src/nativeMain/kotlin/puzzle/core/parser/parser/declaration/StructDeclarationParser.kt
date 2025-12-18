package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.parameter.parameter.parseStructParameters
import puzzle.core.token.kinds.BracketKind.End.RBRACE
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStructDeclaration(header: DeclarationHeader, start: SourceLocation): StructDeclaration {
	val name = parseIdentifierExpression(IdentifierTarget.STRUCT)
	val parameters = parseStructParameters()
	val members = if (cursor.match(LBRACE)) {
		buildList {
			while (!cursor.match(RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	val end = cursor.previous.location
	return StructDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		parameters = parameters,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members,
		location = start span end
	)
}