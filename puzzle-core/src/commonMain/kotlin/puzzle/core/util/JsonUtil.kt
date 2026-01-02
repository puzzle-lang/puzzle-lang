package puzzle.core.util

import kotlinx.serialization.json.Json
import kotlin.time.measureTimedValue

val json = Json {
	prettyPrint = true
	encodeDefaults = true
	classDiscriminator = "class"
	ignoreUnknownKeys = true
}

inline fun <reified T> T.alsoLog(): T {
	if (this == null) {
		println(null)
		return this
	}
	
	val value = measureTimedValue {
		json.encodeToString(this)
	}
	println(value.value)
	return this
}