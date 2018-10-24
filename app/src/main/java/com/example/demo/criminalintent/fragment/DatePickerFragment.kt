package com.example.demo.criminalintent.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.example.demo.criminalintent.R
import android.widget.DatePicker
import java.util.*
import android.app.Activity

class DatePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments!!.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var v: View = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)
        var mDatePicker = v.findViewById(R.id.dialog_data_picker) as DatePicker
        mDatePicker.init(year, month, day, null)

        return AlertDialog.Builder(activity!!)
            .setView(v)
            .setTitle(R.string.date_picker_title)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val year = mDatePicker.year
                val month = mDatePicker.month
                val day = mDatePicker.dayOfMonth
                val date = GregorianCalendar(year, month, day).time
                sendResult(Activity.RESULT_OK, date)
            }
            .create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        if (targetFragment == null) {
            return
        }
        var intent: Intent = Intent()
        intent.putExtra(EXTRA_DATE, date)
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {
        private const val ARG_DATE = "date"
        const val EXTRA_DATE = "com.bignerdranch.android.criminalintent.date"
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}