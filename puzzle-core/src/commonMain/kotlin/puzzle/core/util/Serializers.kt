package puzzle.core.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import puzzle.core.frontend.token.kinds.AssignmentKind
import puzzle.core.frontend.token.kinds.ModifierKind
import puzzle.core.frontend.token.kinds.OperatorKind
import puzzle.core.frontend.token.kinds.SymbolKind

object DotStringListSerializer : KSerializer<List<String>> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		DotStringListSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: List<String>) {
		encoder.encodeString(value.joinToString("."))
	}
	
	override fun deserialize(decoder: Decoder): List<String> {
		return decoder.decodeString().split(".")
	}
}

object SymbolKindSerializer : KSerializer<SymbolKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		SymbolKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: SymbolKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): SymbolKind {
		val symbol = decoder.decodeString()
		return SymbolKind.kinds.first { it.value == symbol }
	}
}

object OperatorKindSerializer : KSerializer<OperatorKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		OperatorKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: OperatorKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): OperatorKind {
		val symbol = decoder.decodeString()
		return OperatorKind.kinds.first { it.value == symbol }
	}
}

object ModifierKindSerializer : KSerializer<ModifierKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		OperatorKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: ModifierKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): ModifierKind {
		val symbol = decoder.decodeString()
		return ModifierKind.kinds.first { it.value == symbol }
	}
}

object AssignmentKindSerializer : KSerializer<AssignmentKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		AssignmentKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: AssignmentKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): AssignmentKind {
		val symbol = decoder.decodeString()
		return AssignmentKind.kinds.first { it.value == symbol }
	}
}