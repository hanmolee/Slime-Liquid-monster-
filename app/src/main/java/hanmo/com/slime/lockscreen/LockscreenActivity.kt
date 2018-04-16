package hanmo.com.slime.lockscreen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.RelativeLayout
import hanmo.com.slime.R
import hanmo.com.slime.db.RealmHelper
import hanmo.com.slime.lockscreen.service.LockScreenViewService
import hanmo.com.slime.util.DLog

/**
 * Created by hanmo on 2018. 4. 16..
 */
class LockscreenActivity : AppCompatActivity() {

    private val TAG : String = "LockScreenActivity"
    private var sLockscreenActivityContext: Context? = null
    private var mLockscreenMainLayout : RelativeLayout? = null
    var mMainHandler: SendMessageHandler? = null

    var phoneStateListener: PhoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {

            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {
                    //아무행동이 없음
                    DLog.e("TelephonyManager.CALL_STATE_IDLE :  $state")
                }
                TelephonyManager.CALL_STATE_RINGING -> {
                    //전화가 오고 있는 상태
                    DLog.e("TelephonyManager.CALL_STATE_RINGING :  $state")
                    LockScreenViewService().dettachLockScreenView()

                }
                else -> {
                    DLog.e("TelephonyManager.ELSE :  $state")
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sLockscreenActivityContext = this
        mMainHandler = SendMessageHandler()

        window.setType(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        val manager : TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)

        initLockScreenUi()

        setLockGuard()

    }

    inner class SendMessageHandler : android.os.Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            finishLockscreenAct()
        }
    }

    private fun finishLockscreenAct() {
        finish()
    }

    private fun initLockScreenUi() {
        setContentView(R.layout.activity_lockscreen)
        mLockscreenMainLayout = findViewById(R.id.lockscreen_main_layout)
        mLockscreenMainLayout?.background?.alpha = 15
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    private fun setLockGuard() {
        var isLockEnable = false
        isLockEnable = LockscreenUtil.getInstance(sLockscreenActivityContext).isStandardKeyguardState

        val startLockscreenIntent = Intent(this, LockScreenViewService::class.java)
        startService(startLockscreenIntent)

        var isSoftkeyEnable = LockscreenUtil.getInstance(sLockscreenActivityContext).isSoftKeyAvail(this)
        RealmHelper.instance.updateIsSoftKey(isSoftkeyEnable)
        if (!isSoftkeyEnable) {
            mMainHandler?.sendEmptyMessage(0)
        } else {
            if (isLockEnable) {
                mMainHandler?.sendEmptyMessage(0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPostResume() {
        super.onPostResume()
    }

}