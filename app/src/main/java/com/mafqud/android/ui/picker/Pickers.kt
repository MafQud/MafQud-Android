package com.mafqud.android.ui.picker

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.mafqud.android.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * @return first String like : 1999-07-12
 * @return second String like : Mon, 1999/07/12
 */
fun datePicker(
    updatedDate: (String, String) -> Unit
): MaterialDatePicker<Long> {
    val currentTimeZone = Calendar.getInstance(TimeZone.getDefault())

    val picker = MaterialDatePicker.Builder.datePicker().setTheme(
        R.style.ThemeOverlay_App_MaterialCalendar
    // limit end date by current day .
    ).setCalendarConstraints(
        CalendarConstraints.Builder().setEnd(currentTimeZone.timeInMillis).build()
    ).setSelection(currentTimeZone.timeInMillis).build()
    picker.addOnPositiveButtonClickListener {
        updatedDate(dateFormatterGlobal(it), dateFormatterDisplay(it))
    }
    return picker
}

fun TimePicker(
    activity: FragmentActivity,
    hourMinute: Pair<Int, Int>,
    onSelect: (Pair<Int, Int>) -> Unit,
    onDismiss: () -> Unit,
) {
    MaterialTimePicker.Builder()
        //TODO change theme
        .setTheme(R.style.TimePacker)
        .setHour(hourMinute.first)
        .setMinute(hourMinute.second)
        .build()
        .apply {
            addOnPositiveButtonClickListener { onSelect(Pair(hour, minute)) }
            addOnDismissListener { onDismiss() }
            show(activity.supportFragmentManager, "tag")
        }
}

@Composable
fun datePicker(onDataPicked: (String) -> Unit): DatePickerDialog {
    val mDate = remember { mutableStateOf("") }
    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    val mDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        R.style.ThemeOverlay_App_MaterialCalendar,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
            onDataPicked(mDate.value)
        }, mYear, mMonth, mDay
    )
    return mDatePickerDialog
}



private fun dateFormatterGlobal(milliseconds: Long?): String {
    milliseconds?.let {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        return formatter.format(calendar.time)
    }
    return ""
}


private fun dateFormatterDisplay(milliseconds: Long?): String {
    milliseconds?.let {
        val formatter = SimpleDateFormat("EEE, yyyy/MM/dd", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        return formatter.format(calendar.time)
    }
    return ""
}