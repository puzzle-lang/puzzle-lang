package puzzle.core.frontend.ast

import kotlinx.io.files.Path
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import puzzle.core.frontend.ast.declaration.Declaration
import puzzle.core.frontend.ast.declaration.ImportDeclaration
import puzzle.core.frontend.ast.declaration.PackageDeclaration

@Serializable
class AstFile(
	@Contextual
	val path: Path?,
	val isBuiltin: Boolean,
	val packageDeclaration: PackageDeclaration?,
	val importDeclarations: List<ImportDeclaration>,
	val declarations: List<Declaration>,
)