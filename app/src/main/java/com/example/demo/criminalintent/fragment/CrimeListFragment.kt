package com.example.demo.criminalintent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.CrimeLab

class CrimeListFragment : Fragment() {
    private lateinit var mCrimeRecyclerView: RecyclerView
    private lateinit var mAdapter: CrimeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager = LinearLayoutManager(activity)
        updateUI()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val crimeLab = CrimeLab[activity!!]
        val crimes = crimeLab.getCrimes()
        when {
            mAdapter != null -> {
                mAdapter.notifyItemChanged(0)
            }
            else -> {
                mAdapter = CrimeAdapter(crimes, this.context!!)
                mCrimeRecyclerView.adapter = mAdapter
            }
        }
    }


}
