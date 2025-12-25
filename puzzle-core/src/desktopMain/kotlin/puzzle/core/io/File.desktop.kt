package puzzle.core.io

import java.io.File

actual fun readFileChars(path: String): CharArray {
	return File(path).readText().toCharArray()
}

actual fun resolveAbsolutePath(path: String): String {
	return File(path).absoluteFile.normalize().path
}