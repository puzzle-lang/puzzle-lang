# PuzzleLang v0.0.0

## 示例

```puzzle
/**
 * 特征 - 消费者
 */
trait Consumable {
	
	fun <T : Food> eat(food: T)
	
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
single Fish : Food {

	override fun getName(): String { "鱼" }
	
}

/**
 * 单例类 - 肉
 */
single Meat : Food {

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

    override fun <T : Food> eat(food: T) {
        println("$name 吃了 ${food.getName()}")
    }
    
    override fun drink(milliliter: Int) {
        println("$name 喝了 $milliliter 毫升水")
    }
}

/**
 * 扩展 - 当特征只有一个函数的时候，可以这么写，更加简洁
 */
fun Animal : InfoProvider.getInfo(): String, Int {
    // 函数的最后一行默认返回，不需要写 return
    this.name, this.age
}

/**
 * 为 Int 实现 toString 函数扩展
 */
fun Int.toString(): String {
    this    // 获取 Int
}

/**
 * 多个类型上下文扩展
 */
fun (_: Parent1, Parent2, parent3: Parent3).run() {
    // 如何获取三个 Parent
    // 第一个无法获取，因为设置了匿名
    // 第二个为默认参数
    this.1              // 这里根据上下文参数顺序，即便前者设置了名称
    // 第三个为指定名称
    this.parent3        // 也可以直接使用 parent3 只要没有名称冲突
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

fun <T : Food> apply(
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
```