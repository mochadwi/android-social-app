package io.mochadwi.util.ext

import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.FragmentTransaction
import io.mochadwi.R
import java.util.*


/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

/**
 * Runs a FragmentTransaction, but check for fragment safety then calls commit().
 * TODO: Set allowStateLoss be true
 */
private inline fun FragmentManager.safeTransact(allowStateLoss: Boolean = false,
                                                action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
        if (!isStateSaved) commit()
        // allowStateLoss mean: https://medium.com/@jacquesgiraudel/the-fragment-is-created-then-put-into-the-back-stack-a48006784e0c
        else if (allowStateLoss) commitAllowingStateLoss()
    }
}

fun AppCompatActivity.setFragmentTag(tagId: Int): String = "$tagId"

fun AppCompatActivity.getFragmentByTag(tagId: Int): Fragment? =
        supportFragmentManager.findFragmentByTag("$tagId")

fun AppCompatActivity.getFragmentByTag(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

inline fun <reified FRAGMENT : Fragment> AppCompatActivity.setFragmentTag(): String? =
        FRAGMENT::class.java.simpleName

inline fun <reified FRAGMENT : Fragment> AppCompatActivity.getFragmentByTag(): Fragment? =
        supportFragmentManager.findFragmentByTag(FRAGMENT::class.java.simpleName)

inline fun <reified FRAGMENT : Fragment> Fragment.setFragmentTag(): String? =
        FRAGMENT::class.java.simpleName

inline fun <reified FRAGMENT : Fragment> Fragment.getFragmentByTag(): Fragment? =
        safeRequireActivity()?.supportFragmentManager?.findFragmentByTag(FRAGMENT::class.java.simpleName)

inline fun <reified FRAGMENT : Fragment> Fragment.getFragmentByTag(tag: String): Fragment? =
        safeRequireActivity()?.supportFragmentManager?.findFragmentByTag(tag)

// TODO: Build add and hide fragment: https://stackoverflow.com/a/16490344/3763032
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment,
                                                frameId: Int,
                                                allowStateLoss: Boolean = false) {
    supportFragmentManager.safeTransact(allowStateLoss) {
        replace(frameId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment,
                                                frameId: Int,
                                                tag: String? = null,
                                                allowStateLoss: Boolean = false,
                                                @AnimRes enterAnimation: Int = R.anim.slide_up,
                                                @AnimRes exitAnimation: Int = R.anim.stay,
                                                @AnimRes popEnterAnimation: Int = R.anim.stay,
                                                @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact(allowStateLoss) {
        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        if (tag == null) replace(frameId, fragment)
        else replace(frameId, fragment, tag)
    }
}

fun AppCompatActivity.replaceFragmentWithBackstack(fragment: Fragment,
                                                   frameId: Int,
                                                   tag: String? = null,
                                                   allowStateLoss: Boolean = false,
                                                   @AnimRes enterAnimation: Int = R.anim.slide_up,
                                                   @AnimRes exitAnimation: Int = R.anim.stay,
                                                   @AnimRes popEnterAnimation: Int = R.anim.stay,
                                                   @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact(allowStateLoss) {
        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        if (tag != null)
            replace(frameId, fragment, tag)
        else
            replace(frameId, fragment)
        addToBackStack(tag)
    }
}

fun AppCompatActivity.addFragmentWithBackStack(fragment: Fragment,
                                               frameId: Int,
                                               tag: String? = null,
                                               allowStateLoss: Boolean = false,
                                               @AnimRes enterAnimation: Int = R.anim.slide_up,
                                               @AnimRes exitAnimation: Int = R.anim.stay,
                                               @AnimRes popEnterAnimation: Int = R.anim.stay,
                                               @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact(allowStateLoss) {
        //        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        if (tag != null)
            add(frameId, fragment, tag)
        else
            add(frameId, fragment)
        addToBackStack(tag)
    }
}

fun Fragment.replaceFragment(fragment: Fragment,
                             frameId: Int,
                             tag: String? = null,
                             allowStateLoss: Boolean = false,
                             @AnimRes enterAnimation: Int = R.anim.slide_up,
                             @AnimRes exitAnimation: Int = R.anim.stay,
                             @AnimRes popEnterAnimation: Int = R.anim.stay,
                             @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    childFragmentManager.safeTransact(allowStateLoss) {
        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        if (tag != null)
            replace(frameId, fragment, tag)
        else
            replace(frameId, fragment)
    }
}


/**
 * Method to replace the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 */
fun AppCompatActivity.replaceFragmentSafely(fragment: Fragment,
                                            tag: String,
                                            allowStateLoss: Boolean = false,
                                            @IdRes containerViewId: Int,
                                            @AnimRes enterAnimation: Int = R.anim.slide_up,
                                            @AnimRes exitAnimation: Int = R.anim.stay,
                                            @AnimRes popEnterAnimation: Int = R.anim.stay,
                                            @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact(allowStateLoss) {
        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        replace(containerViewId, fragment, tag)
    }
}

/**
 * For bottomnav
 */
fun AppCompatActivity.hideFragmentSafely(activeFragment: Fragment,
                                         fragment: Fragment,
                                         tag: String?,
                                         allowStateLoss: Boolean = false,
                                         @IdRes containerViewId: Int,
                                         @AnimRes enterAnimation: Int = R.anim.slide_up,
                                         @AnimRes exitAnimation: Int = R.anim.stay,
                                         @AnimRes popEnterAnimation: Int = R.anim.stay,
                                         @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact(allowStateLoss = allowStateLoss) {
        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        hide(activeFragment)
        show(fragment)
    }
}

/**
 * For bottomnav
 */
fun AppCompatActivity.setupFragmentSafely(fragment: Fragment,
                                          tag: String?,
                                          allowStateLoss: Boolean = false,
                                          @IdRes containerViewId: Int,
                                          @AnimRes enterAnimation: Int = R.anim.slide_up,
                                          @AnimRes exitAnimation: Int = R.anim.stay,
                                          @AnimRes popEnterAnimation: Int = R.anim.stay,
                                          @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact(allowStateLoss = allowStateLoss) {
        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        add(containerViewId, fragment, tag)
    }
}

fun AppCompatActivity.setupHideFragmentSafely(fragment: Fragment,
                                              tag: String?,
                                              allowStateLoss: Boolean = false,
                                              @IdRes containerViewId: Int,
                                              @AnimRes enterAnimation: Int = R.anim.slide_up,
                                              @AnimRes exitAnimation: Int = R.anim.stay,
                                              @AnimRes popEnterAnimation: Int = R.anim.stay,
                                              @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact(allowStateLoss = allowStateLoss) {
        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        add(containerViewId, fragment, tag)
        hide(fragment)
    }
}


fun AppCompatActivity.popAllBackstackIfExists() {
    supportFragmentManager.apply {
        //        while (backStackEntryCount > 0) {
//            popBackStackImmediate()
//        }
    }
}

fun AppCompatActivity.popPreviousBackStack() {
    supportFragmentManager.apply {
        popBackStackImmediate()
    }
}

fun AppCompatActivity.popNamedBackStack(name: String?) {
    supportFragmentManager.apply {
        popBackStackImmediate(name, POP_BACK_STACK_INCLUSIVE)
//        popBackStackImmediate(name, 0)
    }
}

fun performNoBackStackTransaction(fragmentManager: FragmentManager, frameId: Int, tag: String, fragment: Fragment) {
    val newBackStackLength = fragmentManager.backStackEntryCount + 1

    fragmentManager.beginTransaction()
            .replace(frameId, fragment, tag)
            .addToBackStack(tag)
            .commit()

    fragmentManager.addOnBackStackChangedListener(object : FragmentManager.OnBackStackChangedListener {
        override fun onBackStackChanged() {
            val nowCount = fragmentManager.backStackEntryCount
            if (newBackStackLength != nowCount) {
                // we don't really care if going back or forward. we already performed the logic here.
                fragmentManager.removeOnBackStackChangedListener(this)

                if (newBackStackLength > nowCount) { // user pressed back
                    fragmentManager.popBackStackImmediate()
                }
            }
        }
    })
}

/*
 * Add the initial fragment, in most cases the first tab in BottomNavigationView
 */
fun AppCompatActivity.addInitialTabFragment(stacks: Map<String, Stack<Fragment>>,
                                            tag: String,
                                            fragment: Fragment,
                                            layoutId: Int,
                                            shouldAddToStack: Boolean) {
    if (shouldAddToStack) {
        stacks[tag]?.push(fragment)
    }
    supportFragmentManager.safeTransact {
        add(layoutId, fragment, tag)
    }
}

/*
 * Add additional tab in BottomNavigationView on click, apart from the initial one and for the first time
 */
fun AppCompatActivity.addAdditionalTabFragment(stacks: Map<String, Stack<Fragment>>,
                                               tag: String,
                                               show: Fragment,
                                               hide: Fragment,
                                               layoutId: Int,
                                               shouldAddToStack: Boolean) {
    if (shouldAddToStack) {
        stacks[tag]?.push(show)
    }
    supportFragmentManager.safeTransact {
        add(layoutId, show, tag)
        show(show)
        hide(hide)
    }
}

/*
 * Hide previous and show current tab fragment if it has already been added
 * In most cases, when tab is clicked again, not for the first time
 */
fun AppCompatActivity.showHideTabFragment(show: Fragment,
                                          hide: Fragment,
                                          @AnimRes enterAnimation: Int = R.anim.slide_up,
                                          @AnimRes exitAnimation: Int = R.anim.stay,
                                          @AnimRes popEnterAnimation: Int = R.anim.stay,
                                          @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact {
        //        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        hide(hide)
        show(show)
    }
}

/*
 * Add fragment in the particular tab stack and show it, while hiding the one that was before
 */
fun AppCompatActivity.addShowHideFragment(stacks: Map<String, Stack<Fragment>>,
                                          tab: String,
                                          tag: String,
                                          show: Fragment,
                                          hide: Fragment,
                                          layoutId: Int,
                                          shouldAddToStack: Boolean,
                                          isRetainCurrentFragment: Boolean = false,
                                          @AnimRes enterAnimation: Int = R.anim.slide_up,
                                          @AnimRes exitAnimation: Int = R.anim.stay,
                                          @AnimRes popEnterAnimation: Int = R.anim.stay,
                                          @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    if (shouldAddToStack) {
        stacks[tab]?.push(show)
    }
    supportFragmentManager.safeTransact {
        //        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        add(layoutId, show, tag)
        show(show)

        if (!isRetainCurrentFragment)
            hide(hide)
    }
}

fun AppCompatActivity.removeFragment(show: Fragment,
                                     remove: Fragment,
                                     @AnimRes enterAnimation: Int = R.anim.slide_up,
                                     @AnimRes exitAnimation: Int = R.anim.stay,
                                     @AnimRes popEnterAnimation: Int = R.anim.stay,
                                     @AnimRes popExitAnimation: Int = R.anim.slide_down) {
    supportFragmentManager.safeTransact {
        //        setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        remove(remove)
        show(show)
    }
}
