package puzzle.core.util

import kotlinx.serialization.json.Json
import kotlin.time.measureTimedValue

val json = Json {
	prettyPrint = true
	encodeDefaults = false
	classDiscriminator = "class"
	ignoreUnknownKeys = true
	explicitNulls = false
}

inline fun <reified T> T.alsoLog(): T {
	if (this == null) return this
	
	val value = measureTimedValue {
		json.encodeToString(this)
	}
	println(value.value)
	println("序列化耗时: ${value.duration}")
	return this
}