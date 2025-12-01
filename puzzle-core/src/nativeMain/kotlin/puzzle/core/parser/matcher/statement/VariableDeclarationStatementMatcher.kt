package puzzle.core.parser.matcher.statement

import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.VariableDeclarationStatement
import puzzle.core.parser.parser.statement.parseVariableDeclarationStatement

object VariableDeclarationStatementMatcher : StatementMatcher<VariableDeclarationStatement> {

    context(cursor: PzlTokenCursor)
    override fun match(): Boolean {
        return cursor.match(PzlTokenType.VAR) || cursor.match(PzlTokenType.VAL)
    }

    context(_: PzlContext, cursor: PzlTokenCursor)
    override fun parse(): VariableDeclarationStatement {
        return parseVariableDeclarationStatement()
    }
}