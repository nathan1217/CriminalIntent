package com.example.demo.criminalintent.fragment

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.Crime

class CrimePoliceHolder(view: View, context: Context) :
    CrimeHolder(view, context) {
    private var mRequirePolice: CheckBox = itemView.findViewById(R.id.crime_police)
    override fun bind(crime: Crime) {
        super.bind(crime)
        mRequirePolice.isChecked = crime.mRequiresPolice
    }
}