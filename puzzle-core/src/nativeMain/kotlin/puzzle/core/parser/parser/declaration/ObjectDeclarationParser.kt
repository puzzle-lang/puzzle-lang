package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ObjectDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.ModifierTarget
import puzzle.core.parser.parser.check
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.expression.tryParseIdentifier
import puzzle.core.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.parser.parser.parameter.parameter.parseParameters
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseObjectDeclaration(
	header: DeclarationHeader,
	start: SourceLocation,
	isMember: Boolean,
): ObjectDeclaration {
	val name = if (isMember) {
		tryParseIdentifier(IdentifierTarget.OBJECT)
	} else {
		parseIdentifier(IdentifierTarget.OBJECT)
	}
	val primaryAnnotationCalls = parseAnnotationCalls()
	val primaryCtorModifiers = parseModifiers()
	primaryCtorModifiers.check(ModifierTarget.CTOR)
	val parameters = parseParameters(ParameterTarget.OBJECT)
	val superTypes = parseSuperTypes()
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo()
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	return ObjectDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		primaryAnnotationCalls = primaryAnnotationCalls,
		primaryCtorModifiers = primaryCtorModifiers,
		parameters = parameters,
		superTypes = superTypes,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		inits = info.inits,
		ctors = info.ctors,
		members = info.members,
		location = start span end
	)
}