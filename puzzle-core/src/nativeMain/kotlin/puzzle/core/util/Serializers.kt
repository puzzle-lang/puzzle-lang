package puzzle.core.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import puzzle.core.parser.ast.declaration.FunName
import puzzle.core.parser.ast.declaration.IdentifierFunName
import puzzle.core.parser.ast.declaration.SymbolFunName
import puzzle.core.token.*

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

object FunNameSerializer : KSerializer<FunName> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		FunNameSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: FunName) {
		encoder.encodeString(
			when (value) {
				is IdentifierFunName -> value.name
				is SymbolFunName -> value.symbol.value
			}
		)
	}
	
	override fun deserialize(decoder: Decoder): FunName {
		val name = decoder.decodeString()
		return if (name.first().isLetter()) {
			IdentifierFunName(name)
		} else {
			SymbolFunName(SymbolKind.kinds.first { it.value == name })
		}
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

object AccessKindSerializer : KSerializer<AccessKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		AccessKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: AccessKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): AccessKind {
		val symbol = decoder.decodeString()
		return AccessKind.kinds.first { it.value == symbol }
	}
}

object VarianceKindSerializer : KSerializer<VarianceKind> {
	
	override val descriptor = PrimitiveSerialDescriptor(
		VarianceKindSerializer::class.qualifiedName!!,
		PrimitiveKind.STRING
	)
	
	override fun serialize(encoder: Encoder, value: VarianceKind) {
		encoder.encodeString(value.value)
	}
	
	override fun deserialize(decoder: Decoder): VarianceKind {
		val symbol = decoder.decodeString()
		return VarianceKind.kinds.first { it.value == symbol }
	}
}

object ModifierKindListSerializer : KSerializer<List<ModifierKind>> {
	
	override val descriptor = SerialDescriptor(
		ModifierKindListSerializer::class.qualifiedName!!,
		ListSerializer(String.serializer()).descriptor
	)
	
	override fun serialize(encoder: Encoder, value: List<ModifierKind>) {
		val listEncoder = encoder.beginCollection(descriptor, value.size)
		value.forEachIndexed { index, modifier ->
			listEncoder.encodeStringElement(descriptor, index, modifier.value)
		}
		listEncoder.endStructure(descriptor)
	}
	
	override fun deserialize(decoder: Decoder): List<ModifierKind> {
		val modifiers = mutableListOf<ModifierKind>()
		val listDecoder = decoder.beginStructure(descriptor)
		while (true) {
			val index = listDecoder.decodeElementIndex(descriptor)
			if (index == CompositeDecoder.DECODE_DONE) {
				listDecoder.endStructure(descriptor)
				return modifiers
			}
			val value = listDecoder.decodeStringElement(descriptor, index)
			modifiers += ModifierKind.kinds.first { it.value == value }
		}
	}
}