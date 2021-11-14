package com.mafqud.android.ui.material

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.mafqud.android.R

import java.text.SimpleDateFormat
import java.util.*

fun DatePicker(
    activity : FragmentActivity,
    updatedDate: (Long?, String?) -> Unit)
{
    val picker = MaterialDatePicker.Builder.datePicker().setTheme(
        R.style.ThemeOverlay_App_MaterialCalendar
    ).build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        updatedDate(it, DateFormater(it))
    }
}
fun TimePicker(
    activity : FragmentActivity,
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

private fun DateFormater(milliseconds : Long?) : String?{
    milliseconds?.let{
        val formatter = SimpleDateFormat("EEE, dd/MM/yyyy", Locale.US)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        return formatter.format(calendar.time)
    }
    return null
}