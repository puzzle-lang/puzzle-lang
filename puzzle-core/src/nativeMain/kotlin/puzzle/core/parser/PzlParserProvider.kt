package puzzle.core.parser

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

abstract class PzlParserProvider<Parser : PzlParser>(
	private val getParser: (PzlTokenCursor) -> Parser
) {
	private val lock = SynchronizedObject()
	
	private var instance: Parser? = null
	
	@OptIn(InternalCoroutinesApi::class)
	fun of(cursor: PzlTokenCursor): Parser = instance ?: synchronized(lock) {
		instance ?: getParser(cursor).also { instance = it }
	}
}

interface PzlParser