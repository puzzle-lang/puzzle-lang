package puzzle.core.parser.node

import kotlinx.serialization.Serializable
import puzzle.core.parser.declaration.Declaration
import puzzle.core.parser.declaration.ImportDeclaration
import puzzle.core.parser.declaration.PackageDeclaration

interface AstNode

@Serializable
data class PzlProgram(
	val files: List<SourceFileNode>
)

@Serializable
data class SourceFileNode(
	val path: String,
	val packageDeclaration: PackageDeclaration,
	val importDeclarations: List<ImportDeclaration>,
	val declarations: List<Declaration>
) : AstNode