package puzzle.core.parser.parser.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.SourceLocation
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.declaration.CtorDeclaration
import puzzle.core.parser.ast.statement.Statement
import puzzle.core.parser.matcher.declaration.DeclarationHeader
import puzzle.core.parser.matcher.statement.ContextualStatementMatcher
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.tryParseIdentifier
import puzzle.core.parser.parser.parameter.parameter.ParameterTarget
import puzzle.core.parser.parser.parameter.parameter.parseParameters
import puzzle.core.parser.parser.statement.parseStatements
import puzzle.core.token.kinds.BracketKind.Start.LBRACE
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseCtorDeclaration(
	header: DeclarationHeader,
	start: SourceLocation,
): CtorDeclaration {
	val name = tryParseIdentifier(IdentifierTarget.CTOR)
	val parameters = parseParameters(ParameterTarget.CTOR)
	val body = buildList<Statement> {
		if (cursor.match(COLON)) {
			this += with(ContextualStatementMatcher) {
				if (match()) parse() else syntaxError("次构造函数的 ':' 后只允许跟 this 或 super", cursor.current)
			}
		}
		if (cursor.match(LBRACE)) {
			this += parseStatements()
		}
	}
	val end = cursor.previous.location
	return CtorDeclaration(
		name = name,
		docComment = header.docComment,
		parameters = parameters,
		modifiers = header.modifiers,
		contextSpec = header.contextSpec,
		annotationCalls = header.annotationCalls,
		body = body,
		location = start span end
	)
}