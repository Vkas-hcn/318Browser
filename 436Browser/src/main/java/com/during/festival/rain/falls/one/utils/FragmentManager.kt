package com.during.festival.rain.falls.one.utils

import androidx.fragment.app.Fragment

object FragmentManager {
    private val fragmentStack = ArrayList<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentStack.add(fragment)
    }

    fun removeFragment(fragment: Fragment) {
        fragmentStack.remove(fragment)
    }

    fun getCurrentFragment(): Fragment? {
        return fragmentStack.lastOrNull()
    }
}
