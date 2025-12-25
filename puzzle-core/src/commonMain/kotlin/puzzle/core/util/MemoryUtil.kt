package puzzle.core.util

expect fun getCurrentMemoryUsage(): MemoryUsage

class MemoryUsage(
	val usageBytes: Long,
) {
	
	override fun toString(): String {
		val sb = StringBuilder()
		val tb = (usageBytes / (1024L * 1024L * 1024L * 1024L)) % 1024L
		if (tb > 0L) {
			sb.append("$tb TB ")
		}
		val gb = (usageBytes / (1024L * 1024L * 1024L)) % 1024L
		if (gb > 0L) {
			sb.append("$gb GB ")
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