package com.example.demo.criminalintent.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.Crime
import android.text.Editable
import android.widget.Button
import android.widget.CheckBox
import com.example.demo.criminalintent.model.CrimeLab
import com.example.demo.criminalintent.CrimeActivity
import java.text.DateFormat
import java.util.*
import android.R.attr.data
import android.app.Activity
import android.app.Instrumentation


class CrimeFragment : Fragment() {
    public var mIsAdd :Boolean = false
    private lateinit var mCrime: Crime
    private lateinit var mTitleField: EditText
    private lateinit var mDateButton: Button
    private lateinit var mSolvedCheckBox: CheckBox
    private lateinit var mPoliceCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId: UUID = arguments!!.getSerializable(ARG_CRIME_ID) as UUID
        mCrime = CrimeLab[context!!].getCrime(crimeId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = view.findViewById(R.id.crime_title)
        mTitleField.setText(mCrime.mTitle)
        mTitleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                mCrime.mTitle = s.toString()
            }

            override fun afterTextChanged(s: Editable) {
                // This one too
            }
        })

        mDateButton = view.findViewById(R.id.crime_date)
        updateDate()
        mDateButton.setOnClickListener { _ ->
            var manager: FragmentManager? = fragmentManager
            var dialog: DatePickerFragment = DatePickerFragment.newInstance(mCrime.mDate)
            dialog.setTargetFragment(this@CrimeFragment, REQUEST_DATE)
            dialog.show(manager, DIALOG_DATE)
        }

        mSolvedCheckBox = view.findViewById(R.id.crime_solved)
        mSolvedCheckBox.isChecked = mCrime.mSolved
        mSolvedCheckBox.setOnCheckedChangeListener { _, isChecked -> mCrime.mSolved = isChecked }

        mPoliceCheckBox = view.findViewById(R.id.crime_police)
        mPoliceCheckBox.isChecked = mCrime.mRequiresPolice
        mPoliceCheckBox.setOnCheckedChangeListener { _, isChecked -> mCrime.mRequiresPolice = isChecked }

        return view
    }

    override fun onPause() {
        super.onPause()
        CrimeLab[activity!!].updateCrime(mCrime)
    }

    private fun updateDate() {
        mDateButton.text = DateFormat.getDateInstance(1).format(mCrime.mDate)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode === REQUEST_DATE) {
            val date = data!!.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime.mDate = date
            updateDate()
        }
    }

    companion object {
        private const val ARG_CRIME_ID: String = "crime_id"
        private const val DIALOG_DATE: String = "DialogDate"
        private const val REQUEST_DATE: Int = 0
        fun newInstance(crimeId: UUID): CrimeFragment {
            var args: Bundle = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)

            var fragment: CrimeFragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}