package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.check
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.parameter.parameter.parseClassParameters
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseClassDeclaration(header: DeclarationHeader, start: SourceLocation): ClassDeclaration {
	val name = parseIdentifier(IdentifierTarget.CLASS)
	val primaryCtorModifiers = parseModifiers()
	primaryCtorModifiers.check(ModifierTarget.MEMBER_CTOR)
	val parameters = parseClassParameters()
	val superTypes = parseSuperTypes()
	val members = if (cursor.match(LBRACE)) parseMemberDeclarations() else emptyList()
	val end = cursor.previous.location
	return ClassDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		primaryCtorModifiers = primaryCtorModifiers,
		parameters = parameters,
		superTypes = superTypes,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members,
		location = start span end
	)
}