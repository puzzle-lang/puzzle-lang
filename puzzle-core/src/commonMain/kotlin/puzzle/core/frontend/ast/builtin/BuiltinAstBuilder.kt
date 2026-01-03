package puzzle.core.frontend.ast.builtin

import puzzle.core.frontend.ast.AstFile
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.Symbol
import puzzle.core.frontend.ast.declaration.*
import puzzle.core.frontend.ast.expression.Identifier
import puzzle.core.frontend.ast.parameter.Parameter
import puzzle.core.frontend.ast.type.*
import puzzle.core.frontend.model.SourceLocation
import puzzle.core.frontend.token.kinds.ModifierKind
import puzzle.core.frontend.token.kinds.SymbolKind
import puzzle.core.util.isIdentifierString

class BuiltinAstBuilder {
	
	val declarations: List<TopLevelAllowedDeclaration>
		field = mutableListOf()
	
	fun appendFun(
		name: String,
		parameters: List<Parameter> = emptyList(),
		returnType: String? = null,
		modifiers: List<ModifierKind> = emptyList(),
	) {
		declarations += FunDeclaration(
			name = when {
				name == "[]" -> MagicFunName(
					kind = MagicKind.GETTER,
					location = SourceLocation.Builtin
				)
				
				name == "[]=" -> MagicFunName(
					kind = MagicKind.SETTER,
					location = SourceLocation.Builtin
				)
				
				name == "<=>" -> MagicFunName(
					kind = MagicKind.COMPARE,
					location = SourceLocation.Builtin
				)
				
				SymbolKind.kinds.any { it.value == name } -> {
					SymbolFunName(
						symbol = Symbol(
							kind = SymbolKind.kinds.first { it.value == name },
							location = SourceLocation.Builtin
						)
					)
				}
				
				else -> {
					if (!name.isIdentifierString()) {
						error("$name 不是合法的表示符")
					}
					IdentifierFunName(
						name = Identifier(
							name = name,
							location = SourceLocation.Builtin
						)
					)
				}
			},
			docComment = null,
			parameters = parameters,
			modifiers = modifiers.map { kind ->
				Modifier(
					kind = kind,
					location = SourceLocation.Builtin
				)
			},
			returnSpec = returnType?.let { returnType ->
				SingleReturnSpec(
					type = TypeReference(
						type = NamedType(
							segments = returnType.split("."),
							location = SourceLocation.Builtin
						),
						isNullable = false,
						location = SourceLocation.Builtin
					)
				)
			},
			extension = null,
			typeSpec = null,
			contextSpec = null,
			annotationCalls = emptyList(),
			body = emptyList(),
			location = SourceLocation.Builtin
		)
	}
	
	fun appendClass(
		name: String,
		superClass: String? = null,
		superTraits: List<String>? = null,
		modifiers: List<ModifierKind> = emptyList(),
		membersBuilder: BuiltinAstBuilder.() -> Unit,
	) {
		val superTypes = mutableListOf<SuperType>()
		if (superClass != null) {
			superTypes += SuperConstructorCall(
				type = NamedType(
					segments = superClass.split("."),
					location = SourceLocation.Builtin
				),
				arguments = emptyList(),
				location = SourceLocation.Builtin
			)
		}
		superTraits?.forEach { name ->
			superTypes += SuperTypeReference(
				type = NamedType(
					segments = name.split("."),
					location = SourceLocation.Builtin
				),
				location = SourceLocation.Builtin
			)
		}
		declarations += ClassDeclaration(
			name = Identifier(
				name = name,
				location = SourceLocation.Builtin
			),
			docComment = null,
			modifiers = modifiers.map { kind ->
				Modifier(
					kind = kind,
					location = SourceLocation.Builtin
				)
			},
			primaryAnnotationCalls = emptyList(),
			primaryCtorModifiers = emptyList(),
			parameters = emptyList(),
			typeSpec = null,
			contextSpec = null,
			annotationCalls = emptyList(),
			superTypes = superTypes,
			withTypes = emptyList(),
			ctors = emptyList(),
			inits = emptyList(),
			members = BuiltinAstBuilder().apply(membersBuilder).declarations,
			location = SourceLocation.Builtin
		)
	}
	
	fun appendStruct(
		name: String,
		superTraits: List<String> = emptyList(),
		membersBuilderAction: BuiltinAstBuilder.() -> Unit,
	) {
		declarations += StructDeclaration(
			name = Identifier(
				name = name,
				location = SourceLocation.Builtin
			),
			docComment = null,
			modifiers = emptyList(),
			primaryAnnotationCalls = emptyList(),
			primaryCtorModifiers = emptyList(),
			parameters = emptyList(),
			superTypes = superTraits.map { name ->
				SuperTypeReference(
					type = NamedType(
						segments = name.split("."),
						location = SourceLocation.Builtin
					),
					location = SourceLocation.Builtin
				)
			},
			typeSpec = null,
			contextSpec = null,
			annotationCalls = emptyList(),
			inits = emptyList(),
			members = BuiltinAstBuilder()
				.apply(membersBuilderAction)
				.declarations,
			location = SourceLocation.Builtin
		)
	}
	
	fun appendTrait(
		name: String,
		superTraits: List<String> = emptyList(),
		modifiers: List<ModifierKind> = emptyList(),
		membersBuilderAction: BuiltinAstBuilder.() -> Unit,
	) {
		declarations += TraitDeclaration(
			name = Identifier(
				name = name,
				location = SourceLocation.Builtin
			),
			docComment = null,
			modifiers = emptyList(),
			superTypes = superTraits.map { name ->
				SuperTypeReference(
					type = NamedType(
						segments = name.split("."),
						location = SourceLocation.Builtin
					),
					location = SourceLocation.Builtin
				)
			},
			typeSpec = null,
			contextSpec = null,
			annotationCalls = emptyList(),
			members = BuiltinAstBuilder()
				.apply(membersBuilderAction)
				.declarations,
			location = SourceLocation.Builtin
		)
	}
	
	fun parameter(name: String, type: String, isNullable: Boolean = false): Parameter {
		return Parameter(
			name = Identifier(
				name = name,
				location = SourceLocation.Builtin
			),
			modifiers = emptyList(),
			type = TypeReference(
				type = NamedType(
					segments = type.split("."),
					location = SourceLocation.Builtin
				),
				isNullable = isNullable,
				location = SourceLocation.Builtin
			),
			annotationCalls = emptyList(),
			quantifier = null,
			defaultExpression = null,
			location = SourceLocation.Builtin
		)
	}
}

fun buildBuiltinAst(
	name: String,
	builderAction: BuiltinAstBuilder.() -> Unit,
): AstFile {
	return AstFile(
		name = name,
		path = null,
		isBuiltin = true,
		packageDeclaration = PackageDeclaration(
			segments = listOf("puzzle"),
			location = SourceLocation.Builtin
		),
		importDeclarations = emptyList(),
		declarations = BuiltinAstBuilder()
			.apply(builderAction)
			.declarations
	)
}