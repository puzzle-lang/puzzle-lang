package puzzle.core.lexer

enum class PzlTokenType(
	val value: String
) {
	// ==================================================
	// 关键字
	// ==================================================
	FUN("fun"),                 // fun
	CLASS("class"),             // class
	SINGLE("single"),           // single
	TRAIT("trait"),             // trait
	STRUCT("struct"),           // struct
	ENUM("enum"),               // enum
	EXTENSION("extension"),     // extension
	ANNOTATION("annotation"),   // annotation
	PRIVATE("private"),         // private
	PROTECTED("protected"),     // protected
	FILE("file"),               // file
	INTERNAL("internal"),       // internal
	MODULE("module"),           // module
	PUBLIC("public"),           // public
	OPEN("open"),               // open
	ABSTRACT("abstract"),       // abstract
	FINAL("final"),             // final
	OVERRIDE("override"),       // override
	CONST("const"),             // const
	IGNORE("ignore"),           // ignore
	ONLY("only"),               // only
	WITH("with"),               // with
	INIT("init"),               // init
	DELETE("delete"),           // delete
	VAR("var"),                 // var
	VAL("val"),                 // val
	IF("if"),                   // if
	ELSE("else"),               // else
	MATCH("match"),             // match
	FOR("for"),                 // for
	WHILE("while"),             // while
	DO("do"),                   // do
	LOOP("loop"),               // loop
	RETURN("return"),           // return
	BREAK("break"),             // break
	CONTINUE("continue"),       // continue
	AS("as"),                   // as
	IS("is"),                   // is
	PACKAGE("package"),         // package
	IMPORT("import"),           // import
	THIS("this"),               // this
	SUPER("super"),             // super
	TRUE("true"),               // true
	FALSE("false"),             // false
	NULL("null"),               // null
	
	// ==================================================
	// 标识符
	// ==================================================
	IDENTIFIER(""),             // 标识符
	
	// ==================================================
	// 字面量
	// ==================================================
	STRING("String"),           // 字符串
	CHAR("Char"),               // 字符
	NUMBER("Number"),           // 数字
	
	// ==================================================
	// 一元运算符
	// ==================================================
	PLUS("+"),                  // +
	MINUS("-"),                 // -
	BANG("!"),                  // !
	BIT_NOT("~"),               // ~
	DOUBLE_PLUS("++"),          // ++
	DOUBLE_MINUS("--"),         // --
	
	// ==================================================
	// 二元运算符
	// ==================================================
	STAR("*"),                  // *
	SLASH("/"),                 // /
	PERCENT("%"),               // %
	DOUBLE_STAR("**"),          // **
	EQUALS("=="),               // ==
	NOT_EQUALS("!="),           // !=
	GT(">"),                    // >
	GT_EQUALS(">="),            // >=
	LT("<"),                    // <
	LT_EQUALS("<="),            // <=
	TRIPLE_EQUALS("==="),       // ===
	TRIPLE_NOT_EQUALS("!=="),   // !==
	IN("~>"),                   // ~>
	NOT_IN("!>"),               // !>
	BIT_AND("&"),               // &
	BIT_OR("|"),                // |
	BIT_XOR("^"),               // ^
	SHL("<<"),                  // <<
	SHR(">>"),                  // >>
	USHR(">>>"),                // >>>
	
	// ==================================================
	// 逻辑运算符
	// ==================================================
	AND("&&"),                  // &&
	OR("||"),                   // ||
	
	// ==============================
	// 三元运算符
	// ==============================
	COLON(":"),                 // :
	QUESTION("?"),              // ?
	ELVIS("?:"),                // ?:
	
	// ==================================================
	// 赋值运算符
	// ==================================================
	ASSIGN("="),                // =
	QUESTION_ASSIGN("?="),      // ?=
	PLUS_ASSIGN("+="),          // +=
	MINUS_ASSIGN("-="),         // -=
	STAR_ASSIGN("*="),          // *=
	SLASH_ASSIGN("/="),         // /=
	PERCENT_ASSIGN("%="),       // %=
	
	// ==================================================
	// 成员访问
	// ==================================================
	DOT("."),                   // .
	QUESTION_DOT("?."),         // ?.
	DOUBLE_COLON("::"),         // ::
	LPAREN("("),                // (
	RPAREN(")"),                // )
	LBRACKET("["),              // [
	RBRACKET("]"),              // ]
	
	// ==============================
	// 分隔符
	// ==============================
	
	COMMA(","),                 // ,
	SEMICOLON(";"),             // ;
	LBRACE("{"),                // {
	RBRACE("}"),                // }
	
	// ==================================================
	// 特殊
	// ==================================================
	AT("@"),                    // @
	ARROW("->"),                // ->
	DOUBLE_DOT(".."),           // ..
	SINGLE_COMMENT("//"),       // //
	MULTI_COMMENT("/**/"),      // /**/
	NEWLINE("\\n"),             // 换行符
	WHITE_SPACE("' '"),         // 空格
	TAB("\\t"),                 // 制表符
	EOF("EOF");                 // 结束符
	
	override fun toString(): String {
		return this.value
	}
}