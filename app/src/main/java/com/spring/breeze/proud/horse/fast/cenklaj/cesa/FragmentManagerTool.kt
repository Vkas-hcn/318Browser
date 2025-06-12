package com.spring.breeze.proud.horse.fast.cenklaj.cesa

import androidx.fragment.app.Fragment

object FragmentManagerTool {
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
