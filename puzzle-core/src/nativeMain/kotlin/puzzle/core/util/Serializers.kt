package puzzle.core.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import puzzle.core.parser.ast.declaration.FunName
import puzzle.core.parser.ast.declaration.IdentifierFunName
import puzzle.core.parser.ast.declaration.OperatorFunName

object DotSeparatedListSerializer : KSerializer<List<String>> {
	
	override val descriptor: SerialDescriptor =
		PrimitiveSerialDescriptor("DotSeparatedList", PrimitiveKind.STRING)
	
	override fun serialize(encoder: Encoder, value: List<String>) {
		encoder.encodeString(value.joinToString("."))
	}
	
	override fun deserialize(decoder: Decoder): List<String> {
		return decoder.decodeString().split(".")
	}
}

object FunNameSerializer : KSerializer<FunName> {
	
	override val descriptor: SerialDescriptor =
		PrimitiveSerialDescriptor("FunName", PrimitiveKind.STRING)
	
	override fun serialize(encoder: Encoder, value: FunName) {
		encoder.encodeString(
			when (value) {
				is IdentifierFunName -> value.name
				is OperatorFunName -> "SYMBOL(${value.operator})"
			}
		)
	}
	
	override fun deserialize(decoder: Decoder): FunName {
		val str = decoder.decodeString()
		return if (str.startsWith("SYMBOL(") && str.endsWith(")")) {
			val value = str.removePrefix("SYMBOL(").removeSuffix(")")
			OperatorFunName(value)
		} else {
			IdentifierFunName(str)
		}
	}
}