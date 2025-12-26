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
import puzzle.core.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.parser.parser.parameter.parameter.parseParameters
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseClassDeclaration(header: DeclarationHeader, start: SourceLocation): ClassDeclaration {
	val name = parseIdentifier(IdentifierTarget.CLASS)
	val primaryAnnotationCalls = parseAnnotationCalls()
	val primaryCtorModifiers = parseModifiers()
	primaryCtorModifiers.check(ModifierTarget.CTOR)
	val parameters = parseParameters(ParameterTarget.CLASS)
	val superTypeSpecifiers = parseSuperTypeSpecifiers(SuperTypeSpecifierTarget.CLASS)
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo()
	} else MemberDeclarationInfo.Empty
	val end = cursor.previous.location
	return ClassDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		primaryAnnotationCalls = primaryAnnotationCalls,
		primaryCtorModifiers = primaryCtorModifiers,
		parameters = parameters,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		superTypeSpecifiers = superTypeSpecifiers,
		ctors = info.ctors,
		inits = info.inits,
		members = info.members,
		location = start span end
	)
}