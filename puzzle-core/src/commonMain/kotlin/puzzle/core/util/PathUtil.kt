package puzzle.core.util

import kotlinx.io.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

val Path.absolutePath: String
	get() = SystemFileSystem.resolve(this).toString()

val Path.isFile: Boolean
	get() = SystemFileSystem.metadataOrNull(this)?.isRegularFile ?: false

val Path.isDirectory: Boolean
	get() = SystemFileSystem.metadataOrNull(this)?.isDirectory ?: false

fun Path.readText(): String {
	return SystemFileSystem.source(this)
		.buffered()
		.use { it.readString() }
}

@OptIn(InternalIoApi::class)
fun Path.writeText(text: String) {
	SystemFileSystem.source(this)
		.buffered()
		.buffer
		.writeString(text)
}

fun Path.createDirectories(mustCreate: Boolean = false) {
	SystemFileSystem.createDirectories(this, mustCreate = mustCreate)
}

fun Path.sink(append: Boolean = false): RawSink {
	return SystemFileSystem.sink(this, append = append)
}

fun Path.exists(): Boolean {
	return SystemFileSystem.exists(this)
}

fun Path.list(): Collection<Path> {
	return SystemFileSystem.list(this)
}

fun Path.delete() {
	if (!this.exists()) return
	if (this.isFile) SystemFileSystem.delete(this)
	if (this.isDirectory) {
		this.list().forEach { it.delete() }
		SystemFileSystem.delete(this)
	}
}