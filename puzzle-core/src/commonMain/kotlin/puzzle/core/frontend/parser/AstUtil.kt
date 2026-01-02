package puzzle.core.frontend.parser

import puzzle.core.frontend.parser.ast.expression.Identifier

val Identifier.isAnonymousBinding: Boolean
	get() = this.name == "_"