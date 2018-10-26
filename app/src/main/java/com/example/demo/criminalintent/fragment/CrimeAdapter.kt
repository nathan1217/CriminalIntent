package com.example.demo.criminalintent.fragment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.Crime
import com.example.demo.criminalintent.model.ItemType



class CrimeAdapter(private var mCrimes: List<Crime>, private val context: Context) :
    RecyclerView.Adapter<CrimeHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        return when (viewType) {
            ItemType.ITEM_TYPE_POLICE.ordinal -> CrimePoliceHolder(
                layoutInflater.inflate(
                    R.layout.list_item_crime_police,
                    parent,
                    false
                ), context
            )
            else -> CrimeHolder(layoutInflater.inflate(R.layout.list_item_crime, parent, false), context)
        }

    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = mCrimes[position]
        holder.bind(crime)
    }

    override fun getItemCount(): Int {
        return mCrimes.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (mCrimes[position].mRequiresPolice) {
            true -> ItemType.ITEM_TYPE_POLICE.ordinal
            else -> ItemType.ITEM_TYPE_NORMAL.ordinal
        }
    }

    fun setCrimes(crimes: List<Crime>) {
        mCrimes = crimes
    }
}