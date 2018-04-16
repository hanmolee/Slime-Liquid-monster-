package hanmo.com.slime.lockscreen

import android.content.Context
import android.content.Intent
import hanmo.com.slime.lockscreen.service.LockScreenService
import hanmo.com.slime.lockscreen.service.LockScreenViewService
import hanmo.com.slime.util.SharedPreferencesUtil

/**
 * Created by hanmo on 2018. 4. 13..
 */
class Lockscreen {
    private var mContext: Context? = null

    private constructor() {
        mContext = null
    }

    private constructor(context: Context) {
        mContext = context
    }

    fun startLockscreenService() {
        //SharedPreferencesUtil.init(mContext)

        val startLockscreenIntent = Intent(mContext, LockScreenService::class.java)
        //        startLockscreenIntent.putExtra(LockscreenService.LOCKSCREENSERVICE_FIRST_START, true);
        mContext?.startService(startLockscreenIntent)

    }

    fun stopLockscreenService() {
        val stopLockscreenViewIntent = Intent(mContext, LockScreenViewService::class.java)
        mContext?.stopService(stopLockscreenViewIntent)
        val stopLockscreenIntent = Intent(mContext, LockScreenService::class.java)
        mContext?.stopService(stopLockscreenIntent)
    }

    companion object {
        val ISSOFTKEY = "ISSOFTKEY"
        val ISLOCK = "ISLOCK"
        private var mLockscreenInstance: Lockscreen? = null

        fun getInstance(context: Context?): Lockscreen? {
            if (mLockscreenInstance == null) {
                mLockscreenInstance = if (null != context) {
                    Lockscreen(context)
                } else {
                    Lockscreen()
                }
            }
            return mLockscreenInstance
        }
    }
}
