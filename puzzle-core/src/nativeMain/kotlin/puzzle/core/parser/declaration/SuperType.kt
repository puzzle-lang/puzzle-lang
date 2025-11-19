package puzzle.core.parser.declaration

import kotlinx.serialization.Serializable
import puzzle.core.parser.expression.Argument
import puzzle.core.parser.node.TypeReference

@Serializable
sealed interface SuperType {
	val type: TypeReference
}

@Serializable
data class SuperClass(
	override val type: TypeReference,
	val arguments: List<Argument> = emptyList(),
) : SuperType

/**
 * HelloWorld
 */
@Serializable
data class SuperTrait(
	override val type: TypeReference,
) : SuperType

// 可消耗特征
interface Consumable {
	
	fun <T : Food> eat(food: T)
	
	fun drink(milliliter: Int)
}

interface Food {
	
	fun getType(): String
}

object Fish : Food {
	override fun getType(): String {
		return "鱼"
	}
	
}

object Meat : Food {
	override fun getType(): String {
		return "肉"
	}
}

interface InfoProvider {
	
	fun getInfo(): String
}

class Animal(
	val name: String
) : Consumable, InfoProvider {
	override fun <T : Food> eat(food: T) {
		TODO("Not yet implemented")
	}
	
	override fun drink(milliliter: Int) {
		TODO("Not yet implemented")
	}
	
	override fun getInfo(): String {
		return name
	}
}

fun <T : Food> run(animal: Animal, food: T, milliliter: Int) {
	animal.eat(food)
	animal.drink(milliliter)
	val info = animal.getInfo()
	println("$info")
}

fun main() {
	val dot = Animal("🐶")
	val cat = Animal("🐱")
	run(dot, Meat, 20)
	run(cat, Fish, 10)
}