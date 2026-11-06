package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.security.SecureTokenStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecureTokenStoreInstrumentedTest {
    @Test
    fun refreshTokenIsEncryptedAndCanBeCleared() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val tokenStore = SecureTokenStore(context)
        tokenStore.clear()
        val token = "refresh-token-that-must-not-be-stored-as-plain-text"

        tokenStore.saveRefreshToken(token)

        assertNotEquals(token, tokenStore.storedCiphertextForTest())
        assertEquals(token, tokenStore.readRefreshToken())
        tokenStore.clear()
        assertNull(tokenStore.readRefreshToken())
    }
}
