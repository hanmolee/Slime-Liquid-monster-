package hanmo.com.slime.lockscreen.utils.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import hanmo.com.slime.R
import hanmo.com.slime.SlimeApplication
import hanmo.com.slime.lockscreen.utils.LockScreenActivity
import hanmo.com.slime.menu.MainActivity
import hanmo.com.slime.util.DLog

/**
 * Created by hanmo on 2018. 4. 18..
 */
class LockScreenService : Service() {

    private var mServiceStartId : Int? = null
    private lateinit var context: Context
    private lateinit var mNM: NotificationManager

    private val mLockscreenReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            context?.let {
                val actionName = intent.action
                DLog.e("ACION NAME : $actionName")
                when(actionName) {
                    Intent.ACTION_SCREEN_OFF -> { startLockScreenActivity() }
                    else -> {  }
                }
            }
        }
    }

    private fun stateReceiver(isStartReceiver : Boolean) {
        if (isStartReceiver) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            registerReceiver(mLockscreenReceiver, filter)
        } else {
            unregisterReceiver(mLockscreenReceiver)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        mNM = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        showNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mServiceStartId = startId
        stateReceiver(true)
        val bundleIntent = intent
        bundleIntent?.let {
            //startLockScreenActivity()
            DLog.e("  onStartCommand intent  existed")

        } ?: kotlin.run {
            DLog.e("  onStartCommand intent NOT existed);")
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        stateReceiver(false)
        mNM.cancel((application as SlimeApplication).notificationId)
    }

    private fun startLockScreenActivity() {
        val startLockScreenActIntent = Intent(context, LockScreenActivity::class.java)
        startLockScreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(startLockScreenActIntent)
    }

    //상태바에 뜨는 아이콘 만들기
    private fun showNotification() {
        val contentIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)

        val notification = Notification.Builder(this)
                .setSmallIcon(R.drawable.lock)
                .setTicker("Ticker")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("타임스프레드")
                .setContentText("오늘 11시간이 흘렀고 13시간 남음")
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .build()

        mNM.notify((application as SlimeApplication).notificationId, notification)
    }



}