package hanmo.com.slime.lockscreen.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import hanmo.com.slime.R
import hanmo.com.slime.lockscreen.LockscreenUtil

/**
 * Created by hanmo on 2018. 4. 15..
 */
class LockScreenViewService : Service() {

    private val LOCK_OPEN_OFFSET_VALUE = 50
    private var mContext : Context? = null
    private var mInflater: LayoutInflater? = null
    private var mLockscreenView: View? = null
    private var mWindowManager: WindowManager? = null
    private var mParams: WindowManager.LayoutParams? = null
    private var mBackgroundLayout: RelativeLayout? = null
    private var mBackgroundInLayout: RelativeLayout? = null
    private var mBackgroundLockImageView: ImageView? = null
    private var mForgroundLayout: RelativeLayout? = null
    private var mStatusBackgruondDummyView: RelativeLayout? = null
    private var mStatusForgruondDummyView: RelativeLayout? = null

    private var mIsLockEnable = false
    private val mIsSoftkeyEnable = false
    private var mDeviceWidth = 0
    private var mDevideDeviceWidth = 0
    private var mLastLayoutX = 0f
    private val mServiceStartId = 0
    private var mMainHandler: SendMessageHandler? = null


    inner class SendMessageHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            changeBackgroundLockView(mLastLayoutX)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mMainHandler = SendMessageHandler()
        if (isLockScreenAble()) {
            mWindowManager?.let {
                mLockscreenView?.apply {
                    it.removeView(this)
                }
                mWindowManager = null
                mParams = null
                mInflater = null
                mLockscreenView = null

            }

            initState()
            initView()
            attachLockScreenView()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        dettachLockScreenView()
    }

    private fun initState() {
        mIsLockEnable = LockscreenUtil.getInstance(mContext).isStandardKeyguardState
        if (mIsLockEnable) {
            mParams = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            )

        } else {
            mParams = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                            or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                    PixelFormat.TRANSLUCENT
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mIsLockEnable && mIsSoftkeyEnable) {
                mParams?.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            } else {
                mParams?.flags = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            }
        } else {
            mParams?.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        }

        mWindowManager?.apply {
            mWindowManager = mContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
    }

    private fun initView() {
        if (mInflater == null) {
            mInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        }

        if (mLockscreenView == null) {
            mLockscreenView = mInflater?.inflate(R.layout.view_lockscreen, null)
        }
    }

    private fun isLockScreenAble() : Boolean? {

    }

}