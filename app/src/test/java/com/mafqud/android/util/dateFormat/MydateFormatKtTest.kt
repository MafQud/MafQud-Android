package com.mafqud.android.util.dateFormat


import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.util.*

class MydateFormatKtTest {

    @Test
    fun `fromFullDateToAnother, when passing a valid data, then return formatted data in (March,13, 2022 at 3-15 PM)`() {
        val initialDate = " 2022-05-23T10:58:08.452511Z"
        val result = fromFullDateToAnother(initialDate, Locale.ENGLISH)

        val expectedResult = "23, May, 2022  10:58 AM"
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `fromFullDateToAnother, when passing null, then return empty string`() {
        val initialDate = null
        val result = fromFullDateToAnother(initialDate, Locale.ENGLISH)

        assertThat(result).isEmpty()
    }

    @Test
    fun `fromFullDateToAnother, when passing a not valid data, then return Error string`() {
        val initialDate = "545saddas.dsadsadsad5a"
        val result =
            fromFullDateToAnother(initialDate, Locale.ENGLISH, reportToFirebase = false)

        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo("Error parse")

    }

    @Test
    fun `fromNormalDateToFull, when passing a valid data yyyy-MM-dd, then return formatted data dd, MMM, yyyy`() {
        val initialDate = "2022-07-12"
        val result = fromNormalDateToFull(initialDate, Locale.ENGLISH)

        val expectedResult = "12, Jul, 2022"
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `fromNormalDateToFull, when passing a not valid data, then return error string`() {
        val initialDates =  mutableListOf<String>()
        initialDates.add("07-12-2022")
        initialDates.add("sdfdsf52dsf")
        initialDates.add("212sdsdd")
        initialDates.add("ff-cc-ssss")
        initialDates.add("07/12/2022")
        initialDates.add("2022,12,07")
        initialDates.add("2022:12:15")

        initialDates.forEach { initialDate ->
            val result = fromNormalDateToFull(initialDate, Locale.ENGLISH)
            assertThat(result).isNotEmpty()
            assertThat(result).isEqualTo("Error parse")
        }
    }


    @Test
    fun `fromGlobalToDisplay, when passing a valid data yyyy-MM-dd, then return formatted data Mon, 1999 07 12`() {
        val initialDate = "2022-12-5"
        val result =
            fromGlobalToDisplay(initialDate, Locale.ENGLISH)

        val expectedResult = "Mon, 2022/12/05"
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `fromGlobalToDisplay, when passing a not valid data, then return error string`() {
        val initialDates =  mutableListOf<String>()
        initialDates.add("07-12-2022")
        initialDates.add("sdfdsf52dsf")
        initialDates.add("212sdsdd")
        initialDates.add("ff-cc-ssss")
        initialDates.add("07/12/2022")
        initialDates.add("2022,12,07")
        initialDates.add("2022:12:15")
        //initialDates.add("2022-12-55")

        initialDates.forEach { initialDate ->
            val result = fromGlobalToDisplay(initialDate, Locale.ENGLISH)
            assertThat(result).isNotEmpty()
            assertThat(result).isEqualTo("Error parse")
        }
    }
}