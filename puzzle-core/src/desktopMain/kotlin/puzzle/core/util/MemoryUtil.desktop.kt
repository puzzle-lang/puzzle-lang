package puzzle.core.util

actual fun getCurrentMemoryUsage(): MemoryUsage {
	val runtime = Runtime.getRuntime()
	val usageBytes = runtime.totalMemory() - runtime.freeMemory()
	return MemoryUsage(usageBytes)
}