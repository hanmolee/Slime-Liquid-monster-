package hanmo.com.slime

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by hanmo on 2018. 4. 7..
 */
class SlimeApplication : MultiDexApplication() {

    var lockScreenShow = false
    var notificationId = 1989

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        initDB()
    }

    private fun initDB() {
        Realm.init(this)

        val realmConfiguration = RealmConfiguration.Builder()
                .name("slime.realm")
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(realmConfiguration)
    }


    override fun onTerminate() {
        super.onTerminate()
        if (!Realm.getDefaultInstance().isClosed) {
            Realm.getDefaultInstance().close()
        }
    }

}