package com.example.demo.criminalintent

import android.support.v4.app.Fragment
import com.example.demo.criminalintent.fragment.CrimeFragment

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CrimeFragment()
    }
}
