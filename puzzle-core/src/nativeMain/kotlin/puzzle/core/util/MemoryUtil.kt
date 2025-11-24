package puzzle.core.util

import kotlinx.cinterop.*
import platform.darwin.*
import kotlin.native.runtime.NativeRuntimeApi

@OptIn(ExperimentalForeignApi::class, NativeRuntimeApi::class)
fun currentMemoryUsage(): MemoryUsage {
	return memScoped {
		val info = alloc<task_basic_info>()
		val size = alloc<mach_msg_type_number_tVar>()
		size.value = sizeOf<task_basic_info>().toUInt() / 4u
		val kerr = task_info(
			mach_task_self_,
			TASK_BASIC_INFO.toUInt(),
			info.ptr.reinterpret(),
			size.ptr
		)
		if (kerr == KERN_SUCCESS) {
			info.resident_size.toLong()
		} else -1L
	}.let(::MemoryUsage)
}

class MemoryUsage(
	val usageBytes: Long
) {
	
	override fun toString(): String {
		val sb = StringBuilder()
		val tb = (usageBytes / (1024L * 1024L * 1024L * 1024L)) % 1024L
		if (tb > 0L) {
			sb.append("$tb TB ")
		}
		val gb = (usageBytes / (1024L * 1024L * 1024L)) % 1024L
		if (gb > 0L) {
			sb.append("$tb GB ")
		}
		val mb = (usageBytes / (1024L * 1024L)) % 1024L
		if (mb > 0L) {
			sb.append("$mb MB ")
		}
		val kb = (usageBytes / 1024L) % 1024L
		if (kb > 0L) {
			sb.append("$kb KB ")
		}
		val b = usageBytes % 1024L
		if (b > 0L) {
			sb.append("$b B")
		}
		return sb.trim().toString()
	}
}