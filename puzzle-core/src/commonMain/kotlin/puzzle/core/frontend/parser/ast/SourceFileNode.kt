package puzzle.core.frontend.parser.ast

import kotlinx.serialization.Serializable
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.parser.ast.declaration.Declaration
import puzzle.core.frontend.parser.ast.declaration.ImportDeclaration
import puzzle.core.frontend.parser.ast.declaration.PackageDeclaration

@Serializable
class SourceFileNode(
	val path: String,
	val packageDeclaration: PackageDeclaration?,
	val importDeclarations: List<ImportDeclaration>,
	val declarations: List<Declaration>,
	override val location: SourceLocation,
) : AstNode