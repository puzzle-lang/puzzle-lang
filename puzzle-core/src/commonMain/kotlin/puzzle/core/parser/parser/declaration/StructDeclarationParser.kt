package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.StructDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.parser.parser.parameter.parameter.parseParameters
import puzzle.core.parser.parser.parseAnnotationCalls
import puzzle.core.parser.parser.parseModifiers
import puzzle.core.parser.parser.type.SuperTypeTarget
import puzzle.core.parser.parser.type.parseSuperTypes
import puzzle.core.parser.parser.type.safeAsSuperTypeReferences
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.ContextualKind.WITH

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseStructDeclaration(header: DeclarationHeader, start: SourceLocation): StructDeclaration {
	val name = parseIdentifier(IdentifierTarget.STRUCT)
	val primaryAnnotationCalls = parseAnnotationCalls()
	val primaryCtorModifiers = parseModifiers()
	val parameters = parseParameters(ParameterTarget.STRUCT)
	val superTypes = parseSuperTypes(SuperTypeTarget.STRUCT)
		.safeAsSuperTypeReferences()
	if (cursor.match(WITH)) {
		syntaxError("结构体不支持 with", cursor.previous)
	}
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo()
	} else MemberDeclarationInfo.Empty
	if (info.ctors.isNotEmpty()) {
		syntaxError("结构体不允许有次构造函数", info.ctors.first())
	}
	val end = cursor.previous.location
	return StructDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		primaryAnnotationCalls = primaryAnnotationCalls,
		primaryCtorModifiers = primaryCtorModifiers,
		parameters = parameters,
		superTypes = superTypes,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		inits = info.inits,
		members = info.members,
		location = start span end
	)
}