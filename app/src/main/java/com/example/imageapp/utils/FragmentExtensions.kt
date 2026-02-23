package com.example.imageapp.utils

/*import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentActivity
import com.example.imageapp.R

fun Fragment.navigateToFragment(
    targetFragment: Fragment,
    tag: String? = null,
    addToBackStack: Boolean = true
) {
    parentFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out
        )
        .replace(R.id.nav_host_fragment, targetFragment)
        .apply {
            if (addToBackStack) addToBackStack(tag)
        }
        .commit()
}




// Extension function for FragmentActivity (Activity with fragments).

fun FragmentActivity.openFragment(
    fragment: Fragment,
    addToBackStack: Boolean = true,
    containerId: Int = R.id.nav_host_fragment
) {
    val transaction = supportFragmentManager.beginTransaction()
        .replace(containerId, fragment)

    if (addToBackStack) {
        transaction.addToBackStack(null)
    }

    transaction.commit()
}

//Extension function for Fragment (so you can call it inside fragments).
fun Fragment.openFragment(
    fragment: Fragment,
    addToBackStack: Boolean = true,
    containerId: Int = R.id.nav_host_fragment
) {
    requireActivity().openFragment(fragment, addToBackStack, containerId)
}



fun Fragment.navigateToChildFragment(
    targetFragment: Fragment,
    tag: String? = null,
    addToBackStack: Boolean = true
) {
    childFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .replace(R.id.nav_host_fragment, targetFragment)
        .apply {
            if (addToBackStack && tag != null) addToBackStack(tag)
        }
        .commit()
}

fun Fragment.popCurrentFragment() {
    parentFragmentManager.popBackStack()
}

fun Fragment.navigateWithSharedElement(
    targetFragment: Fragment,
    sharedView: View,
    sharedElementName: String,
    tag: String? = null,
    addToBackStack: Boolean = true
) {
    parentFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .addSharedElement(sharedView, sharedElementName)
        .replace(R.id.nav_host_fragment, targetFragment)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .apply {
            if (addToBackStack && tag != null) addToBackStack(tag)
        }
        .commit()
}*/

