# PuzzleLang v0.0.0

## 示例

```puzzle
/**
 * 特征 - 消费者
 */
trait Consumable {

    type<T : Food>
	fun eat(food: T)
	
	fun drink(milliliter: Int)
}

/**
 * 特征 - 食物
 */
trait Food {
	
	fun getName(): String
}

/**
 * 单例类 - 鱼
 */
object Fish : Food {

	override fun getName(): String { "鱼" }
	
}

/**
 * 单例类 - 肉
 */
object Meat : Food {

	override fun getName(): String { "肉" }
}

/**
 * 特征 - 信息提供者
 */
trait InfoProvider {
	
	fun getInfo(): String, Int
}

/**
 * 类 - 动物
 */
class Animal(
	protected val name: String,
	protected val age: Int
)

/**
 * 扩展 - 为 Animal 扩展 Consumable 特征
 */
extension Animal : Consumable {

    type<T : Food>
    override fun eat(food: T) {
        println("$name 吃了 ${food.getName()}")
    }
    
    override fun drink(milliliter: Int) {
        println("$name 喝了 $milliliter 毫升水")
    }
}

/**
 * 扩展函数
 */
fun Int.toString(): String {
    this    // 获取 Int
}

fun test() {
    // 使用 Int.toString()
    10.toString()
    
    // 使用 (Parent1, Parent2).run()
    val parent1 = Parent1()
    val parent2 = Parent2()
    
    // 显示调用
    (parent1, parent2).run()
    
    // 隐式调用
    (parent1, parent2) {
        run()   // 你可以不需要指定，因为上下文已经被锁定了
        run()   // 多次调用
    }
}

type<T : Food>
fun apply(
    animal: Animal?,
    food: T, 
    milliliter: Int, 
    onFinished: (name: String, age: Int) -> Unit
) {
	animal.eat(food)
	animal.drink(milliliter)
	// 自动解构多返回值，当你不需要某个参数的时候，也可以使用匿名 _
	val name, age = animal.getInfo()
	// 没错 lambda 也支持指定名称传递参数
	onFinished(name = name, age = age)
}

/**
 * 结构体
 */
struct Info(
    val name: String,
    val age: String
)

/**
 * 编译期计算常量函数
 *
 * 入参和返回值必须为结构体，且必须有返回类型
 * 可以在编译期计算，无运行时性能
 * 被普通函数调用，和普通函数行为相同
 */
const fun getNextYearInfo(name: String, age: Int): Info {
    Info(name, age + 1)
}

/*
 * 当入参在编译期确定且为结构体，可在编译期计算结果
 */
private const val FishInfo = getInfo("鱼", 1)

fun main() {
	val dot = Animal("猫咪")
	val cat = Animal("小狗")
	apply(dot, Meat, 2) { // name, age ->           // 名称根据函数定义，可以不写
	    println("$name 今年 $age 岁了，吃完了生日餐")
	}
	apply(cat, Fish, 3) { catName, _ ->             // 当然你也可以手动指定，不需要的时候，也可以使用匿名
	    println("catName 的饭吃好了")
	}
}

reified type<T>                  // reified 关键字表示泛型具体化，运行时支持获取泛型类型
class Generic1(
    private val type: T
) {
    
    init {
        println(T::type == String::type)        // 支持运行时获取泛型类型信息
        println(T::type < Object::type)         // 支持是否是某个类型的子类型
    }
}

type<T>
class Generic2(
    private val type: T
) {

    init {
        // println(T::type)                     // error! 泛型为具体化，不支持判断类型
    }
}

@Annotation1
@Annotation2(name = "Hi")
reified type<T>
fun generic3(type: T) {
    println(type is Int)
}

fun useGeneric() {
    val generic1 = Generic1(1)  // 对已经泛型具体化的类，直接调用即可，会对所有调用者生成具体化的类
    
    val generic2 = reified Generic2(2)  // 对于未泛型具体化的类，需要泛型信息时，可以使用 reified 关键字定义，会为调用类型生成具体化类
    println(generic2 is Generic2<Int>)  // 这里可以为运行时提供泛型判断功能，但是未具体化的类不行
    
    generic3("Hello")                   // 对泛型具体化的函数正常调用即可，编译器会自动生成所有调用点类型的具体化类实现
}
```