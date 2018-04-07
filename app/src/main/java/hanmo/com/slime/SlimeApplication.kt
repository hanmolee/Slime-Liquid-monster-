package hanmo.com.slime

import android.support.multidex.MultiDexApplication

/**
 * Created by hanmo on 2018. 4. 7..
 */
class SlimeApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}