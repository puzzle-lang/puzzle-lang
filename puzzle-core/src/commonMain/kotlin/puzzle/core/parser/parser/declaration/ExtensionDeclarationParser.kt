package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.ExtensionDeclaration
import puzzle.core.parser.ast.declaration.SuperTrait
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.BracketKind.Start.LBRACE

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseExtensionDeclaration(header: DeclarationHeader, start: SourceLocation): ExtensionDeclaration {
	val extendedType = parseTypeReference()
	val superTraits = parseSuperTypes(allowClass = false)
		.filterIsInstance<SuperTrait>()
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo()
	} else MemberDeclarationInfo.Empty
	if (info.inits.isNotEmpty()) {
		syntaxError("扩展不允许有初始化块", info.inits.first())
	}
	if (info.ctors.isNotEmpty()) {
		syntaxError("扩展不允许有次构造函数", info.ctors.first())
	}
	val end = cursor.previous.location
	return ExtensionDeclaration(
		extendedType = extendedType,
		modifiers = header.modifiers,
		superTraits = superTraits,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		members = info.members,
		location = start span end
	)
}