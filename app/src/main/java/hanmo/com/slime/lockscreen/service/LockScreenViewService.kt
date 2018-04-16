package hanmo.com.slime.lockscreen.service

import android.annotation.TargetApi
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.os.Message
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import hanmo.com.slime.R
import hanmo.com.slime.db.RealmHelper
import hanmo.com.slime.lockscreen.LockscreenUtil
import hanmo.com.slime.lockscreen.PermissionActivity
import hanmo.com.slime.util.DLog

/**
 * Created by hanmo on 2018. 4. 15..
 */
class LockScreenViewService : Service() {
    private val LOCK_OPEN_OFFSET_VALUE = 50
    private var mContext : Context? = null
    private var mInflater : LayoutInflater? = null
    private var mLockscreenView : View? = null
    private var mWindowManager : WindowManager? = null
    private var mParams : WindowManager.LayoutParams? = null
    private var mBackgroundLayout : RelativeLayout? = null
    private var mBackgroundInLayout : RelativeLayout? = null
    private var mBackgroundLockImageView : ImageView? = null
    private var mForgroundLayout : RelativeLayout? = null
    private var mStatusBackgruondDummyView : RelativeLayout? = null
    private var mStatusForgruondDummyView : RelativeLayout? = null
    private var mShimmerTextView : TextView? = null
    private var mIsLockEnable = false
    private val mIsSoftkeyEnable = false
    private var mDeviceWidth = 0
    private var mDevideDeviceWidth = 0
    private var mLastLayoutX = 0f
    private val mServiceStartId = 0
    private var mMainHandler : SendMassgeHandler? = null

    private val isLockScreenAble : Boolean
        get() {
            var isLock = RealmHelper.instance.getLockScreenIsLock()
            DLog.e("잠금 확인!:   $isLock")
            isLock = isLock
            return isLock!!
        }

    private val isAttachedToWindow : Boolean
        @TargetApi(Build.VERSION_CODES.M)
        get() = mLockscreenView?.isAttachedToWindow!!


    private val mViewTouchListener = object : View.OnTouchListener {
        private var firstTouchX = 0f
        private var layoutPrevX = 0f
        private var lastLayoutX = 0f
        private var layoutInPrevX = 0f
        private var isLockOpen = false
        private var touchMoveX = 0
        private val touchInMoveX = 0

        override fun onTouch(v : View, event : MotionEvent) : Boolean {

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {// 0
                    firstTouchX = event.x
                    layoutPrevX = mForgroundLayout?.x!!
                    layoutInPrevX = mBackgroundLockImageView?.x!!
                    if (firstTouchX <= LOCK_OPEN_OFFSET_VALUE) {
                        isLockOpen = true
                    }
                }
                MotionEvent.ACTION_MOVE -> { // 2
                    if (isLockOpen) {
                        touchMoveX = (event.rawX - firstTouchX).toInt()
                        if (mForgroundLayout?.x!! >= 0) {
                            mForgroundLayout?.x = (layoutPrevX + touchMoveX)
                            mBackgroundLockImageView?.x = (layoutInPrevX + (touchMoveX / 1.8)).toFloat()
                            mLastLayoutX = lastLayoutX
                            mMainHandler?.sendEmptyMessage(0)
                            if (mForgroundLayout?.x!! < 0) {
                                mForgroundLayout?.x = 0f
                            }
                            lastLayoutX = mForgroundLayout?.x!!
                        }
                    } else {
                        return false
                    }
                }
                MotionEvent.ACTION_UP -> { // 1
                    if (isLockOpen) {
                        mForgroundLayout?.x = lastLayoutX
                        mForgroundLayout?.y = 0f
                        optimizeForground(lastLayoutX)
                    }
                    isLockOpen = false
                    firstTouchX = 0f
                    layoutPrevX = 0f
                    layoutInPrevX = 0f
                    touchMoveX = 0
                    lastLayoutX = 0f
                }
                else -> {}
            }

            return true
        }
    }
    //    private boolean sIsSoftKeyEnable = false;

    private inner class SendMassgeHandler:android.os.Handler() {
        override fun handleMessage(msg:Message) {
            super.handleMessage(msg)
            changeBackGroundLockView(mLastLayoutX)
        }
    }



    override fun onBind(intent:Intent) : IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        mContext = this
        //SharedPreferencesUtil.init(mContext)
        //        sIsSoftKeyEnable = SharedPreferencesUtil.get(Lockscreen.ISSOFTKEY);
    }

    override fun onStartCommand(intent : Intent, flags : Int, startId : Int) : Int {
        mMainHandler = SendMassgeHandler()
        if (isLockScreenAble) {
            if (null != mWindowManager) {
                if (null != mLockscreenView) {
                    mWindowManager?.removeView(mLockscreenView)
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
                    PixelFormat.TRANSLUCENT)
        } else {
            mParams = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                            or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                    PixelFormat.TRANSLUCENT)
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

        if (null == mWindowManager) {
            mWindowManager = (mContext?.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        }
    }

    private fun initView() {
        if (null == mInflater) {
            mInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (null == mLockscreenView) {
            mLockscreenView = mInflater?.inflate(R.layout.view_lockscreen, null)

        }
    }


    private fun attachLockScreenView() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mContext)) {
                val permissionActivityIntent = Intent(mContext, PermissionActivity::class.java)
                permissionActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(permissionActivityIntent)

                LockscreenUtil.getInstance(mContext).getPermissionCheckSubject()
                        ?.subscribe { addLockScreenView() }
            } else {
                addLockScreenView()
            }
        }
        else
        {
            addLockScreenView()
        }

    }

    private fun addLockScreenView() {
        if (null != mWindowManager && null != mLockscreenView && null != mParams) {
            mLockscreenView!!.setOnTouchListener { v, event -> false }
            mWindowManager?.addView(mLockscreenView, mParams)
            settingLockView()
        }
    }


    fun dettachLockScreenView() : Boolean {

        return if (null != mWindowManager && null != mLockscreenView && isAttachedToWindow) {
            DLog.e("detach true!!")
            mWindowManager?.removeView(mLockscreenView)
            mLockscreenView = null
            mWindowManager = null
            stopSelf(mServiceStartId)
            true
        } else {
            DLog.e("detach  false!!")
            false
        }
    }

    private fun settingLockView() {
        mBackgroundLayout = mLockscreenView?.findViewById<View>(R.id.lockscreen_background_layout) as RelativeLayout
        mBackgroundInLayout = mLockscreenView?.findViewById<View>(R.id.lockscreen_background_in_layout) as RelativeLayout
        mBackgroundLockImageView = mLockscreenView?.findViewById<View>(R.id.lockscreen_background_image) as ImageView
        mForgroundLayout = mLockscreenView?.findViewById<View>(R.id.lockscreen_forground_layout) as RelativeLayout
        mShimmerTextView = mLockscreenView?.findViewById<View>(R.id.shimmer_tv) as TextView
        //(Shimmer()).start(mShimmerTextView)
        mForgroundLayout?.setOnTouchListener(mViewTouchListener)

        mStatusBackgruondDummyView = mLockscreenView?.findViewById<View>(R.id.lockscreen_background_status_dummy) as RelativeLayout
        mStatusForgruondDummyView = mLockscreenView?.findViewById<View>(R.id.lockscreen_forground_status_dummy) as RelativeLayout
        setBackGroundLockView()

        val displayMetrics = mContext?.resources?.displayMetrics
        mDeviceWidth = displayMetrics?.widthPixels!!
        mDevideDeviceWidth = (mDeviceWidth / 2)
        mBackgroundLockImageView?.x = (((mDevideDeviceWidth) * -1)).toFloat()

        //kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val statusHeight = LockscreenUtil.getInstance(mContext).statusBarHeight
            val forgroundParam = mStatusForgruondDummyView?.layoutParams as RelativeLayout.LayoutParams
            forgroundParam.height = statusHeight
            mStatusForgruondDummyView?.layoutParams = forgroundParam
            val alpha = AlphaAnimation(0.5f, 0.5f)
            alpha.duration = 0 // Make animation instant
            alpha.fillAfter = true // Tell it to persist after the animation ends
            mStatusForgruondDummyView?.startAnimation(alpha)
            val backgroundParam = mStatusBackgruondDummyView?.layoutParams as RelativeLayout.LayoutParams
            backgroundParam.height = statusHeight
            mStatusBackgruondDummyView?.layoutParams = backgroundParam
        }
    }

    private fun setBackGroundLockView() {
        if (mIsLockEnable) {
            mBackgroundInLayout?.setBackgroundColor(resources.getColor(R.color.lock_background_color))
            mBackgroundLockImageView?.visibility = View.VISIBLE

        } else {
            mBackgroundInLayout?.setBackgroundColor(resources.getColor(android.R.color.transparent))
            mBackgroundLockImageView?.visibility = View.GONE
        }
    }


    private fun changeBackGroundLockView(forgroundX : Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (forgroundX < mDeviceWidth) {
                mBackgroundLockImageView?.background = resources.getDrawable(R.drawable.lock)
            } else {
                mBackgroundLockImageView?.background = resources.getDrawable(R.drawable.unlock)
            }
        } else {
            if (forgroundX < mDeviceWidth) {
                mBackgroundLockImageView?.setBackgroundDrawable(resources.getDrawable(R.drawable.lock))
            } else {
                mBackgroundLockImageView?.setBackgroundDrawable(resources.getDrawable(R.drawable.unlock))
            }
        }
    }

    private fun optimizeForground(forgroundX : Float) {
        //        final int devideDeviceWidth = (mDeviceWidth / 2);
        if (forgroundX < mDevideDeviceWidth) {
            var startPostion = 0
            startPostion = mDevideDeviceWidth
            while (startPostion >= 0) {
                mForgroundLayout?.x = startPostion.toFloat()
                startPostion--
            }
        } else {
            val animation = TranslateAnimation(0f, mDevideDeviceWidth.toFloat(), 0f, 0f)
            animation.duration = 300
            animation.setAnimationListener(object:Animation.AnimationListener {
                override fun onAnimationStart(animation:Animation) {}

                override fun onAnimationEnd(animation:Animation) {
                    mForgroundLayout?.x = mDevideDeviceWidth.toFloat()
                    mForgroundLayout?.y = 0f
                    DLog.e("detach 시켜라")
                    dettachLockScreenView()
                }

                override fun onAnimationRepeat(animation:Animation) {}
            })

            mForgroundLayout?.startAnimation(animation)
        }
    }
}
