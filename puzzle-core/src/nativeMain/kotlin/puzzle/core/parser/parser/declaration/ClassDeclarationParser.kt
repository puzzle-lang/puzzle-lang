package puzzle.core.parser.parser.declaration

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ClassDeclaration
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.parser.parser.modifier.ModifierTarget
import puzzle.core.parser.parser.modifier.check
import puzzle.core.parser.parser.modifier.parseModifiers
import puzzle.core.parser.parser.parameter.parameter.parseClassParameters
import puzzle.core.token.BracketKind

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseClassDeclaration(header: DeclarationHeader): ClassDeclaration {
	val name = parseIdentifierName(IdentifierNameTarget.CLASS)
	val constructorModifiers = parseModifiers()
	constructorModifiers.check(ModifierTarget.CONSTRUCTOR_FUN)
	val parameters = parseClassParameters()
	val superTypes = parseSuperTypes()
	val members = if (cursor.match(BracketKind.Start.LBRACE)) {
		buildList {
			while (!cursor.match(BracketKind.End.RBRACE)) {
				this += parseMemberDeclaration()
			}
		}
	} else emptyList()
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
		members = members
	)
}