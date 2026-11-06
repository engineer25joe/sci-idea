package com.example.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class BackendUrlPolicyTest {
    @Test
    fun `production accepts only the approved https host`() {
        assertEquals(
            "https://sci-idea-backend.onrender.com/",
            BackendUrlPolicy.validate("https://sci-idea-backend.onrender.com", isDebug = false)
        )
        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate("http://sci-idea-backend.onrender.com", isDebug = false)
        }
        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate("https://attacker.example", isDebug = false)
        }
    }

    @Test
    fun `development accepts local hosts but rejects arbitrary hosts and credentials`() {
        assertEquals(
            "http://10.0.2.2:8000/",
            BackendUrlPolicy.validate("http://10.0.2.2:8000", isDebug = true)
        )
        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate("http://attacker.example", isDebug = true)
        }
        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate("https://user:pass@localhost:8000", isDebug = true)
        }
    }
}
