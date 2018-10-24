package com.example.demo.criminalintent.fragment

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.demo.criminalintent.CrimeActivity
import com.example.demo.criminalintent.CrimePagerActivity
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.Crime
import java.text.DateFormat

open class CrimeHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

    private val mTitleTextView: TextView = itemView.findViewById(R.id.crime_title)
    private val mDateTextView: TextView = itemView.findViewById(R.id.crime_date)
    private val mSolveImageView: ImageView = itemView.findViewById(R.id.imageView)
    private lateinit var mCrime: Crime

    init {
        itemView.setOnClickListener { _ ->
            val intent: Intent = CrimePagerActivity.newIntent(context, mCrime.mId)
            startActivity(context, intent, null)
        }
    }

    open fun bind(crime: Crime) {
        mCrime = crime
        mTitleTextView.text = crime.mTitle
        mDateTextView.text = DateFormat.getDateInstance(0).format(crime.mDate)
        mSolveImageView.visibility = when (crime.mSolved) {
            true -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }
}