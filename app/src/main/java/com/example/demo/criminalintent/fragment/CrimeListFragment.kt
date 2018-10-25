package com.example.demo.criminalintent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.CrimeLab
import com.example.demo.criminalintent.CrimePagerActivity
import com.example.demo.criminalintent.model.Crime


class CrimeListFragment : Fragment() {
    private lateinit var mCrimeRecyclerView: RecyclerView
    private lateinit var mAdapter: CrimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager = LinearLayoutManager(activity)
        updateUI()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab[activity!!].addCrime(crime)
                val intent = CrimePagerActivity.newIntent(activity!!, crime.mId)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val crimeLab = CrimeLab[activity!!]
        val crimes = crimeLab.getCrimes()

        mAdapter = CrimeAdapter(crimes, this.context!!)
        mCrimeRecyclerView.adapter = mAdapter
    }
}


