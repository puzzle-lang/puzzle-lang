package puzzle.core.token

import kotlinx.serialization.Serializable
import puzzle.core.collections.fastSetOf
import puzzle.core.collections.mergeFastSets

@Serializable
sealed interface KeywordKind : PzlTokenKind {
	
	companion object {
		val kinds = mergeFastSets(
			ModifierKind.kinds,
			NamespaceKind.kinds,
			DeclarationKind.kinds,
			AccessorKind.kinds,
			ControlFlowKind.kinds,
			JumpKind.kinds,
			ContextualKind.kinds,
			LiteralKind.keywordKinds
		)
	}
}

@Serializable
sealed class ModifierKind(
	override val value: String,
	val order: Int
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<ModifierKind>(
			PRIVATE, PROTECTED, FILE, INTERNAL, MODULE, PUBLIC,
			FINAL,
			OPEN, ABSTRACT, SEALED, OVERRIDE,
			CONST, OWNER, IGNORE, LATE, LAZY, ARGS,
			VAR, VAL
		)
	}
	
	@Serializable
	object PRIVATE : ModifierKind("private", 0)
	
	@Serializable
	object PROTECTED : ModifierKind("protected", 0)
	
	@Serializable
	object FILE : ModifierKind("file", 0)
	
	@Serializable
	object INTERNAL : ModifierKind("internal", 0)
	
	@Serializable
	object MODULE : ModifierKind("module", 0)
	
	@Serializable
	object PUBLIC : ModifierKind("public", 0)
	
	@Serializable
	object FINAL : ModifierKind("final", 1)
	
	@Serializable
	object OPEN : ModifierKind("open", 2)
	
	@Serializable
	object ABSTRACT : ModifierKind("abstract", 2)
	
	@Serializable
	object SEALED : ModifierKind("sealed", 2)
	
	@Serializable
	object OVERRIDE : ModifierKind("override", 2)
	
	@Serializable
	object CONST : ModifierKind("const", 3)
	
	@Serializable
	object OWNER : ModifierKind("owner", 3)
	
	@Serializable
	object IGNORE : ModifierKind("ignore", 3)
	
	@Serializable
	object LATE : ModifierKind("late", 3)
	
	@Serializable
	object LAZY : ModifierKind("lazy", 3)
	
	@Serializable
	object ARGS : ModifierKind("args", 3)
	
	@Serializable
	object VAR : ModifierKind("var", 4)
	
	@Serializable
	object VAL : ModifierKind("val", 4)
}

@Serializable
sealed class NamespaceKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<NamespaceKind>(PACKAGE, IMPORT)
	}
	
	@Serializable
	object PACKAGE : NamespaceKind("package")
	
	@Serializable
	object IMPORT : NamespaceKind("import")
}

@Serializable
sealed class DeclarationKind(
	override val value: String,
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<DeclarationKind>(FUN, CLASS, UNIQUE, TRAIT, STRUCT, ENUM, EXTENSION, ANNOTATION)
	}
	
	@Serializable
	object FUN : DeclarationKind("fun")
	
	@Serializable
	object CLASS : DeclarationKind("class")
	
	@Serializable
	object UNIQUE : DeclarationKind("unique")
	
	@Serializable
	object TRAIT : DeclarationKind("trait")
	
	@Serializable
	object STRUCT : DeclarationKind("struct")
	
	@Serializable
	object ENUM : DeclarationKind("enum")
	
	@Serializable
	object EXTENSION : DeclarationKind("extension")
	
	@Serializable
	object ANNOTATION : DeclarationKind("annotation")
}

@Serializable
sealed class AccessorKind(
	override val value: String
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<AccessorKind>(GET, SET)
	}
	
	@Serializable
	object GET : AccessorKind("get")
	
	@Serializable
	object SET : AccessorKind("set")
}

@Serializable
sealed class ControlFlowKind(
	override val value: String
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<ControlFlowKind>(IF, ELSE, MATCH, FOR, WHILE, DO, LOOP)
	}
	
	@Serializable
	object IF : ControlFlowKind("if")
	
	@Serializable
	object ELSE : ControlFlowKind("else")
	
	@Serializable
	object MATCH : ControlFlowKind("match")
	
	@Serializable
	object FOR : ControlFlowKind("for")
	
	@Serializable
	object WHILE : ControlFlowKind("while")
	
	@Serializable
	object DO : ControlFlowKind("do")
	
	@Serializable
	object LOOP : ControlFlowKind("loop")
}

@Serializable
sealed class JumpKind(
	override val value: String
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<JumpKind>(BREAK, CONTINUE, RETURN)
	}
	
	@Serializable
	object BREAK : JumpKind("break")
	
	@Serializable
	object CONTINUE : JumpKind("continue")
	
	@Serializable
	object RETURN : JumpKind("return")
}

@Serializable
sealed class ContextualKind(
	override val value: String
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<ContextualKind>(TYPE, REIFIED, CONTEXT, INIT, DELETE, AS, IS, THIS, SUPER)
	}
	
	@Serializable
	object TYPE : ContextualKind("type")
	
	@Serializable
	object REIFIED : ContextualKind("reified")
	
	@Serializable
	object CONTEXT : ContextualKind("context")
	
	@Serializable
	object INIT : ContextualKind("init")
	
	@Serializable
	object DELETE : ContextualKind("delete")
	
	@Serializable
	object AS : ContextualKind("as")
	
	@Serializable
	object IS : ContextualKind("is")
	
	@Serializable
	object THIS : ContextualKind("this")
	
	@Serializable
	object SUPER : ContextualKind("super")
}

@Serializable
sealed class VarianceKind(
	override val value: String
) : KeywordKind {
	
	companion object {
		
		val kinds = fastSetOf<VarianceKind>(IN, OUT)
	}
	
	@Serializable
	object IN : VarianceKind("in")
	
	@Serializable
	object OUT : VarianceKind("out")
}