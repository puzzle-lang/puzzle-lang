package puzzle.core.io

expect fun readFileChars(path: String): CharArray

expect fun resolveAbsolutePath(path: String): String