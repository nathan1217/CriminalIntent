package com.example.demo.criminalintent.fragment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.Crime

open class CrimeHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

    private val mTitleTextView: TextView = itemView.findViewById(R.id.crime_title)
    private val mDateTextView: TextView = itemView.findViewById(R.id.crime_date)
    private lateinit var mCrime: Crime

    init {
        itemView.setOnClickListener { _ ->
            Toast.makeText(context, mCrime.mTitle + " clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    open fun bind(crime: Crime) {
        mCrime = crime
        mTitleTextView.text = crime.mTitle
        mDateTextView.text = crime.mDate.toString()
    }
}