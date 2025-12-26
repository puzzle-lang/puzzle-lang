package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.MixinDeclaration
import puzzle.core.parser.ast.type.NamedType
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.parser.parser.type.parseNamedType
import puzzle.core.parser.parser.type.parseWithTypes
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.ContextualKind.ON
import puzzle.core.token.kinds.SeparatorKind.COMMA

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseMixinDeclaration(header: DeclarationHeader, start: SourceLocation): MixinDeclaration {
	val name = parseIdentifier(IdentifierTarget.MIXIN)
	val mixinConstraints = parseMixinConstraints()
	val withTypes = parseWithTypes()
	val info = if (cursor.match(LBRACE)) {
		parseMemberDeclarationInfo()
	} else MemberDeclarationInfo.Empty
	if (info.inits.isNotEmpty()) {
		syntaxError("混入不允许有初始化块", info.inits.first())
	}
	if (info.ctors.isNotEmpty()) {
		syntaxError("混入不允许有次构造函数", info.ctors.first())
	}
	val end = cursor.previous.location
	return MixinDeclaration(
		name = name,
		docComment = header.docComment,
		modifiers = header.modifiers,
		typeSpec = header.typeSpec,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		mixinConstraints = mixinConstraints,
		withTypes = withTypes,
		members = info.members,
		location = start span end
	)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseMixinConstraints(): List<NamedType> {
	if (!cursor.match(ON)) return emptyList()
	return buildList {
		do {
			this += parseNamedType()
		} while (cursor.match(COMMA))
	}
}