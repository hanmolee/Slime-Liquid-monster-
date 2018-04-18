package hanmo.com.slime.lockscreen.utils.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import hanmo.com.slime.util.DLog

/**
 * Created by hanmo on 2018. 4. 18..
 */
class LockScreen {
    internal var context: Context? = null
    internal var disableHomeButton = false


    internal var prefs: SharedPreferences? = null
    val isActive: Boolean
        get() = if (context != null) {
            isMyServiceRunning(LockScreenService::class.java)
        } else {
            false
        }

    fun init(context: Context) {
        this.context = context


    }

    fun init(context: Context, disableHomeButton: Boolean) {
        this.context = context
        this.disableHomeButton = disableHomeButton

    }

    //이부분 조
    /*private fun showSettingAccesability() {
        if (!isMyServiceRunning(LockWindowAccessibilityService::class.java)) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            context!!.startActivity(intent)
        }
    }*/


    fun active() {
        if (disableHomeButton) {
            //showSettingAccesability()
        }

        if (context != null) {
            context!!.startService(Intent(context, LockScreenService::class.java))
        }
    }

    fun deactivate() {
        if (context != null) {
            context!!.stopService(Intent(context, LockScreenService::class.java))
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object {
        private var singleton: LockScreen? = null

        val instance: LockScreen
            get() {
                if (singleton == null) {
                    singleton = LockScreen()

                }
                return singleton as LockScreen
            }
    }


}