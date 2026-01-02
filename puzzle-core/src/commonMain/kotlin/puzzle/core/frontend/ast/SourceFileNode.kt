package puzzle.core.frontend.ast

import kotlinx.io.files.Path
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.ast.declaration.ImportDeclaration
import puzzle.core.frontend.ast.declaration.PackageDeclaration
import puzzle.core.frontend.model.SourceLocation

@Serializable
class SourceFileNode(
	@Contextual
	val path: Path,
	val packageDeclaration: PackageDeclaration?,
	val importDeclarations: List<ImportDeclaration>,
	val declarations: List<Declaration>,
	override val location: SourceLocation,
) : AstNode