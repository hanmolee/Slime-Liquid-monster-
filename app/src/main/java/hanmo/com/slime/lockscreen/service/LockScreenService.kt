package hanmo.com.slime.lockscreen.service

import android.app.KeyguardManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.TelephonyManager
import hanmo.com.slime.util.DLog

/**
 * Created by hanmo on 2018. 4. 13..
 */
class LockScreenService : Service() {

    private val TAG = "LockscreenService"
    private var mServiceStartId = 0
    private var mContext: Context? = null

    private val mLockscreenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (null != context) {
                if (intent.action == Intent.ACTION_SCREEN_OFF) {
                    val startLockscreenIntent = Intent(mContext, LockscreenViewService::class.java)
                    stopService(startLockscreenIntent)
                    val tManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val isPhoneIdle = tManager.callState == TelephonyManager.CALL_STATE_IDLE
                    if (isPhoneIdle) {
                        startLockscreenActivity()
                    }
                }
            }
        }
    }

    fun stateReceiver(isStartReceiver : Boolean) {
        if (isStartReceiver) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            registerReceiver(mLockscreenReceiver, filter)
        } else {
            mLockscreenReceiver.let {
                unregisterReceiver(it)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mServiceStartId = startId
        stateReceiver(true)
        val bundleIntent = intent

        if (bundleIntent == null) {
            DLog.e("$TAG onStartCommand intent NOT existed")
        }

        bundleIntent?.let {
            startLockscreenActivity()
        }

        return START_STICKY
    }

    fun setLockGuard() {
        initKeyguardService()
        if (!LockScreenUtil.instance(mContext).isStandardKeyguardState()) {
            setStandardKeyguardState(false)
        } else {
            setStandardKeyguardState(true)
        }
    }

    private var mKeyManager : KeyguardManager? = null
    private var mKeyLock : KeyguardManager.KeyguardLock? = null

    private fun initKeyguardService() {
        mKeyManager?.let {
            mKeyManager = null
        }
        mKeyManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager?
        mKeyManager?.let {
            mKeyLock?.let {
                mKeyLock = null
            }
            mKeyLock = mKeyManager?.newKeyguardLock(Context.KEYGUARD_SERVICE)
        }
    }

    private fun setStandardKeyguardState(isStart : Boolean) {
        if (isStart) {
            mKeyLock?.let {
                mKeyLock?.reenableKeyguard()
            }
        } else {
            mKeyManager?.let {
                mKeyLock?.disableKeyguard()
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        stateReceiver(false)
        setStandardKeyguardState(true)
    }

    private fun startLockscreenActivity() {
        val startLockScreenActIntent = Intent(mContext, LockScreenActivity::class.java)
        startLockScreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(startLockScreenActIntent)
    }

}