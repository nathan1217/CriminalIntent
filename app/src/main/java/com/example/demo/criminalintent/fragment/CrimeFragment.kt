package com.example.demo.criminalintent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
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


class CrimeFragment : Fragment() {
    private lateinit var mCrime: Crime
    private lateinit var mTitleField: EditText
    private lateinit var mDateButton: Button
    private lateinit var mSolvedCheckBox: CheckBox
    private lateinit var mPoliceCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = activity!!.intent.getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID) as UUID
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
        mDateButton.text = DateFormat.getDateInstance(1).format(mCrime.mDate)
        mDateButton.isEnabled = false

        mSolvedCheckBox = view.findViewById(R.id.crime_solved)
        mSolvedCheckBox.isChecked = mCrime.mSolved
        mSolvedCheckBox.setOnCheckedChangeListener { _, isChecked -> mCrime.mSolved = isChecked }

        mPoliceCheckBox = view.findViewById(R.id.crime_police)
        mPoliceCheckBox.isChecked = mCrime.mRequiresPolice
        mPoliceCheckBox.setOnCheckedChangeListener { _, isChecked -> mCrime.mRequiresPolice = isChecked }

        return view
    }
}