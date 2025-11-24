@file:OptIn(ExperimentalForeignApi::class)

package puzzle.core.io

import kotlinx.cinterop.*
import platform.posix.*

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
	
	fun readText(): String {
		val file = fopen(path, "rb") ?: error("无法打开文件: $path")
		try {
			val buffers = StringBuilder()
			memScoped {
				val buf = allocArray<ByteVar>(4096)
				while (true) {
					val bytesRead = fread(buf, 1u, 4096u, file)
					if (bytesRead == 0UL) break
					val chunk = buf.readBytes(bytesRead.toInt()).decodeToString()
					buffers.append(chunk)
				}
			}
			return buffers.toString()
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