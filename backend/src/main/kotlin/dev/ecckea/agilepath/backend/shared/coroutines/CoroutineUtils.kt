package dev.ecckea.agilepath.backend.shared.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Executes a given suspending block of code within the context of the `Dispatchers.IO` coroutine dispatcher.
 *
 * This utility function is useful for performing IO-bound operations, such as reading from or writing to a database,
 * file system, or making network requests, ensuring that such operations are executed on a thread pool optimized
 * for IO tasks.
 *
 * @param T The return type of the suspending block.
 * @param block A suspending lambda function to be executed within the `Dispatchers.IO` context.
 * @return The result of the executed block.
 */
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.IO, block)