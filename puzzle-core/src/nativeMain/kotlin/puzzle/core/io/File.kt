@file:OptIn(ExperimentalForeignApi::class)

package puzzle.core.io

import kotlinx.cinterop.*
import platform.posix.*
import kotlin.math.min

class File(
	private val path: String
) {
	
	private var _absolutePath: String? = null
	
	val absolutePath: String
		get() {
			if (_absolutePath != null) return _absolutePath!!
			return memScoped {
				val resolved = realpath(path, null)
				if (resolved != null) {
					val absPath = resolved.toKString()
					free(resolved)
					return absPath
				}
				val combined = if (path.startsWith("/")) {
					path
				} else {
					val cwd = currentWorkingDirectory()
					"$cwd/$path"
				}
				val normalized = realpath(combined, null)
				if (normalized != null) {
					val abs = normalized.toKString()
					free(normalized)
					return abs
				}
				combined
			}.also { _absolutePath = it }
		}
	
	fun readChars(): CharArray {
		val file = fopen(path, "rb") ?: error("无法打开文件: $path")
		try {
			fseek(file, 0, SEEK_END)
			val size = ftell(file).toInt()
			rewind(file)
			val bytes = ByteArray(size)
			memScoped {
				val pageSize = min(size, sysconf(_SC_PAGESIZE).toInt())
				val buf = allocArray<ByteVar>(pageSize)
				var offset = 0
				while (true) {
					val toRead = minOf(pageSize, size - offset)
					val bytesRead = fread(buf, 1u, toRead.toULong(), file).toInt()
					if (bytesRead == 0) break
					repeat(bytesRead) {
						bytes[offset + it] = buf[it]
					}
					offset += bytesRead
				}
			}
			return bytes.decodeToString().toCharArray()
		} finally {
			fclose(file)
		}
	}
}

fun currentWorkingDirectory(): String {
	val cwdPtr = getcwd(null, 0u)
	val cwd = cwdPtr?.toKString() ?: "."
	if (cwdPtr != null) free(cwdPtr)
	return cwd
}