package puzzle.core.frontend.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.AssignmentKind
import puzzle.core.frontend.token.kinds.ModifierKind
import puzzle.core.frontend.token.kinds.OperatorKind
import puzzle.core.frontend.token.kinds.SymbolKind
import puzzle.core.util.AssignmentKindSerializer
import puzzle.core.util.ModifierKindSerializer
import puzzle.core.util.OperatorKindSerializer
import puzzle.core.util.SymbolKindSerializer

@Serializable
class Symbol(
	@Serializable(with = SymbolKindSerializer::class)
	val kind: SymbolKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Modifier(
	@Serializable(with = ModifierKindSerializer::class)
	val kind: ModifierKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Operator(
	@Serializable(with = OperatorKindSerializer::class)
	val kind: OperatorKind,
	override val location: SourceLocation,
) : AstNode

@Serializable
class Assignment(
	@Serializable(with = AssignmentKindSerializer::class)
	val kind: AssignmentKind,
	override val location: SourceLocation
) : AstNode