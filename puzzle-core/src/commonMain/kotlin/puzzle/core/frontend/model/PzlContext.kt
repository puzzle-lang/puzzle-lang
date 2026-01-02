package puzzle.core.frontend.model

import kotlinx.io.files.Path

class PzlContext(
	val sourcePath: Path,
	var lineStarts: IntArray,
)