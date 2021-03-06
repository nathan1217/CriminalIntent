package com.example.demo.criminalintent.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.demo.criminalintent.R
import com.example.demo.criminalintent.model.CrimeLab
import com.example.demo.criminalintent.CrimePagerActivity
import com.example.demo.criminalintent.model.Crime
import android.support.v7.app.AppCompatActivity

class CrimeListFragment : Fragment() {
    private lateinit var mCrimeRecyclerView: RecyclerView
    private var mAdapter: CrimeAdapter? = null
    private var mSubtitleVisible: Boolean = false
    private var mCallBacks: CallBacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager = LinearLayoutManager(activity)
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }
        updateUI()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.fragment_crime_list, menu)
        var subtitleItem: MenuItem = menu!!.findItem(R.id.show_subtitle)
        when {
            mSubtitleVisible -> subtitleItem.setTitle(R.string.hide_subtitle)
            else -> subtitleItem.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab[activity!!].addCrime(crime)
                updateUI()
                mCallBacks!!.onCrimeSelected(crime)
                true
            }
            R.id.show_subtitle -> {
                mSubtitleVisible = !mSubtitleVisible
                activity!!.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun updateSubtitle() {
        val crimeLab = CrimeLab[activity!!]
        val crimeCount = crimeLab.getCrimes().size
        var subtitle: String? = getString(R.string.subtitle_format, crimeCount)
        if (!mSubtitleVisible) {
            subtitle = null
        }
        val activity = activity as AppCompatActivity?
        activity!!.supportActionBar!!.subtitle = subtitle
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible)
    }

    fun updateUI() {
        val crimeLab = CrimeLab[activity!!]
        val crimes = crimeLab.getCrimes()
        if (mAdapter == null) {
            mAdapter = CrimeAdapter(crimes, this.context!!, mCallBacks!!)
            mCrimeRecyclerView.adapter = mAdapter
        } else {
            mAdapter!!.setCrimes(crimes)
            mAdapter!!.notifyDataSetChanged()
        }
        updateSubtitle()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallBacks = activity as CallBacks
    }

    override fun onDetach() {
        super.onDetach()
        mCallBacks = null
    }

    interface CallBacks {
        fun onCrimeSelected(crime: Crime)
    }

    companion object {
        private const val SAVED_SUBTITLE_VISIBLE: String = "subtitle"
    }
}


