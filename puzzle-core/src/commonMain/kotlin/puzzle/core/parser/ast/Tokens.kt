package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.model.SourceLocation
import puzzle.core.token.kinds.AssignmentKind
import puzzle.core.token.kinds.ModifierKind
import puzzle.core.token.kinds.OperatorKind
import puzzle.core.token.kinds.SymbolKind
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