package hanmo.com.slime.lockscreen.utils

import android.app.Application

/**
 * Created by hanmo on 2018. 4. 18..
 */
class LockApplication : Application() {

    var lockScreenShow = false
    var notificationId = 1989

    override fun onCreate() {
        super.onCreate()
    }
}