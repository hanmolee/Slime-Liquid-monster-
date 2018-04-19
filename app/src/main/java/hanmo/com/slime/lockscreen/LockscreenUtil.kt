package hanmo.com.slime.lockscreen

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import rx.subjects.PublishSubject

/**
 * Created by hanmo on 2018. 4. 15..
 */
class LockscreenUtil {
    private var mContext: Context? = null

    private var permissionCheckSubject: PublishSubject<Boolean>? = null

    val isStandardKeyguardState: Boolean
        get() {
            var isStandardKeyguqrd = false
            val keyManager = mContext?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (null != keyManager) {
                isStandardKeyguqrd = keyManager.isKeyguardSecure
            }

            return isStandardKeyguqrd
        }

    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = mContext?.resources?.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId!! > 0)
                result = mContext?.resources?.getDimensionPixelSize(resourceId)!!

            return result
        }

    private constructor() {
        mContext = null
    }

    private constructor(context: Context) {
        mContext = context
    }

    fun isSoftKeyAvail(context: Context): Boolean {
        val isSoftkey = booleanArrayOf(false)
        val activityRootView = (context as Activity).window.decorView.findViewById<View>(android.R.id.content)
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rootViewHeight = activityRootView.rootView.height
            val viewHeight = activityRootView.height
            val heightDiff = rootViewHeight - viewHeight
            if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
                isSoftkey[0] = true
            }
        }
        return isSoftkey[0]
    }

    fun getPermissionCheckSubject(): PublishSubject<Boolean>? {
        if (null == permissionCheckSubject) {
            permissionCheckSubject = PublishSubject.create()
        }

        return permissionCheckSubject
    }

    companion object {
        private var mLockscreenUtilInstance: LockscreenUtil? = null

        fun getInstance(context: Context?): LockscreenUtil {
            if (mLockscreenUtilInstance == null) {
                if (null != context) {
                    mLockscreenUtilInstance = LockscreenUtil(context)
                } else {
                    mLockscreenUtilInstance = LockscreenUtil()
                }
            }
            return mLockscreenUtilInstance as LockscreenUtil
        }
    }
}
