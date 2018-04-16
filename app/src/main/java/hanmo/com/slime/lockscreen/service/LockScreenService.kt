package hanmo.com.slime.lockscreen.service

import android.app.KeyguardManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log
import hanmo.com.slime.lockscreen.LockScreenView
import hanmo.com.slime.lockscreen.LockscreenActivity
import hanmo.com.slime.lockscreen.LockscreenUtil

/**
 * Created by hanmo on 2018. 4. 13..
 */
class LockScreenService : Service() {
    private val TAG = "LockscreenService"
    //    public static final String LOCKSCREENSERVICE_FIRST_START = "LOCKSCREENSERVICE_FIRST_START";
    private var mServiceStartId = 0
    private var mContext: Context? = null


    private val mLockscreenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (null != context) {
                if (intent.action == Intent.ACTION_SCREEN_OFF) {
                    val startLockscreenIntent = Intent(mContext, LockScreenViewService::class.java)
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

    private var mKeyManager: KeyguardManager? = null
    private var mKeyLock: KeyguardManager.KeyguardLock? = null

    private fun stateRecever(isStartRecever: Boolean) {
        if (isStartRecever) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            registerReceiver(mLockscreenReceiver, filter)
        } else {
            unregisterReceiver(mLockscreenReceiver)

        }
    }


    override fun onCreate() {
        super.onCreate()
        mContext = this
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mServiceStartId = startId
        stateRecever(true)
        if (null != intent) {
            startLockscreenActivity()
        } else {
            Log.d(TAG, "$TAG onStartCommand intent NOT existed")
        }
        setLockGuard()
        return START_STICKY
    }


    private fun setLockGuard() {
        initKeyguardService()
        if (!LockscreenUtil.getInstance(mContext).isStandardKeyguardState) {
            setStandardKeyguardState(false)
        } else {
            setStandardKeyguardState(true)
        }
    }

    private fun initKeyguardService() {
        if (null != mKeyManager) {
            mKeyManager = null
        }
        mKeyManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        if (null != mKeyManager) {
            if (null != mKeyLock) {
                mKeyLock = null
            }
            mKeyLock = mKeyManager?.newKeyguardLock(KEYGUARD_SERVICE)
        }
    }

    private fun setStandardKeyguardState(isStart: Boolean) {
        if (isStart) {
            if (null != mKeyLock) {
                mKeyLock?.reenableKeyguard()
            }
        } else {

            if (null != mKeyManager) {
                mKeyLock?.disableKeyguard()
            }
        }
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onDestroy() {
        stateRecever(false)
        setStandardKeyguardState(true)
    }

    private fun startLockscreenActivity() {
        val startLockscreenActIntent = Intent(mContext, LockscreenActivity::class.java)
        startLockscreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(startLockscreenActIntent)
    }

}