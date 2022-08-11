package com.mafqud.android.util.validation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidatorKtTest {


    @Test
    fun `isValidEmail, when passing a valid emails, then return true`() {
        val emails = mutableListOf<String>()
        emails.add("a@gmail.com")
        emails.add("a@yahoo.com")
        emails.add("a@inst.com")
        emails.add("a@mafqud.com")
        emails.add("a@hotmail.com")



        emails.forEach { email ->
            val result = isValidEmail(email)
            assertThat(result).isTrue()
        }
    }
}