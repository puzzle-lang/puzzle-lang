package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.parser.parser.parameter.parameter.parseClassParameters
import puzzle.core.token.kinds.BracketKind.End.RBRACE
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseClassDeclaration(header: DeclarationHeader, start: SourceLocation): ClassDeclaration {
	val name = parseIdentifierExpression(IdentifierTarget.CLASS)
	val constructorModifiers = parseModifiers()
	constructorModifiers.check(ModifierTarget.CONSTRUCTOR_FUN)
	val parameters = parseClassParameters()
	val superTypes = parseSuperTypes()
	val members = if (cursor.match(LBRACE)) {
		buildList {
			while (!cursor.match(RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
	val end = cursor.previous.location
	return ClassDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		constructorModifiers = constructorModifiers,
		parameters = parameters,
		superTypes = superTypes,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = members,
		location = start span end
	)
}