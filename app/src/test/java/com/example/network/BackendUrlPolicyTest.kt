package com.example.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class BackendUrlPolicyTest {

    @Test
    fun `accepts only approved production backend`() {
        assertEquals(
            "https://sci-idea-backend.onrender.com/",
            BackendUrlPolicy.validate(
                "https://sci-idea-backend.onrender.com"
            )
        )

        assertEquals(
            "https://sci-idea-backend.onrender.com/",
            BackendUrlPolicy.validate(
                "https://sci-idea-backend.onrender.com/"
            )
        )
    }

    @Test
    fun `rejects http connections`() {
        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate(
                "http://sci-idea-backend.onrender.com"
            )
        }
    }

    @Test
    fun `rejects unapproved hosts`() {
        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate(
                "https://attacker.example"
            )
        }

        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate(
                "http://10.0.2.2:8000"
            )
        }
    }

    @Test
    fun `rejects credentials and query parameters`() {
        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate(
                "https://user:pass@sci-idea-backend.onrender.com"
            )
        }

        assertThrows(IllegalArgumentException::class.java) {
            BackendUrlPolicy.validate(
                "https://sci-idea-backend.onrender.com?debug=true"
            )
        }
    }
}