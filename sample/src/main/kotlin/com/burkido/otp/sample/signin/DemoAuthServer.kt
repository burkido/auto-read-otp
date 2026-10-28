package com.burkido.otp.sample.signin

import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object DemoAuthServer {

    const val CODE_LENGTH = 6

    val codeLifetime: Duration = 60.seconds

    fun issueCode(): String = buildString { repeat(CODE_LENGTH) { append(Random.nextInt(10)) } }

    fun wrongCodeFor(code: String): String = code.map { '0' + (it.digitToInt() + 1) % 10 }.joinToString("")

    suspend fun sendCode(): Unit = delay(900)

    suspend fun verify(code: String, expected: String): Boolean {
        delay(650)
        return code == expected
    }
}
