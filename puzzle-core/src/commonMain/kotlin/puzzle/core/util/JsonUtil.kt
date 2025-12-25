package puzzle.core.util

import kotlinx.serialization.json.Json

val json = Json {
	prettyPrint = true
	encodeDefaults = false
	classDiscriminator = "class"
	ignoreUnknownKeys = true
	explicitNulls = false
}

inline fun <reified T> T.alsoLog(): T {
	if (this == null) return this
	println(json.encodeToString(this))
	return this
}