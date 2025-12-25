package puzzle.core.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.parser.ast.declaration.Declaration
import puzzle.core.model.SourceLocation
import puzzle.core.parser.ast.declaration.ImportDeclaration
import puzzle.core.parser.ast.declaration.PackageDeclaration

@Serializable
class SourceFileNode(
	val path: String,
	val packageDeclaration: PackageDeclaration,
	val importDeclarations: List<ImportDeclaration>,
	val declarations: List<Declaration>,
	override val location: SourceLocation,
) : AstNode