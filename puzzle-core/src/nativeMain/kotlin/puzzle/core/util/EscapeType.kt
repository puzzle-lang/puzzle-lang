package puzzle.core.util

enum class EscapeType(
	val escape: String
) {
	SINGLE_QUOTE("\\'"),
	DOUBLE_QUOTE("\\\""),
	BACKSLASH("\\\\"),
	NEWLINE("\\n"),
	CARRIAGE_RETURN("\\r"),
	TAB("\\t"),
	BACKSPACE("\\b"),
	FORM_FEED("\\f"),
	UNICODE("\\u");
	
	companion object {
		
		val standardEscapes = entries.filter { it != UNICODE }.map { it.escape }
	}
}