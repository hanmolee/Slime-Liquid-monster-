package hanmo.com.slime.lockscreen.utils

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import hanmo.com.slime.R
import hanmo.com.slime.SlimeApplication
import hanmo.com.slime.util.DLog
import hanmo.com.slime.util.RippleViewCreator
import kotlinx.android.synthetic.main.activity_lockscreen.*
import com.jakewharton.rxbinding2.view.clicks
import hanmo.com.slime.menu.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import android.view.animation.Animation
import android.view.animation.TranslateAnimation


/**
 * Created by hanmo on 2018. 4. 18..
 */
class LockScreenActivity : AppCompatActivity() {

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lockscreen)

        RippleViewCreator.addRippleToView(rippleTest)

    }

    private fun getMenuList() {
        val menuList = arrayOf("배경사진 바꾸기", "사용방법 다시보기", "문의하기", "설정")

        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(applicationContext, resId)

        with(lcMenuList) {
            layoutAnimation = animation
            setHasFixedSize(true)
            layoutManager = android.support.v7.widget.LinearLayoutManager(applicationContext)
            adapter = LockScreenMenuAdapter(menuList)
        }
    }

    private fun setMenuButton() {

        lockscreenMenuButton.clicks()
                .throttleFirst(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    if (lcMenuList.visibility == View.GONE){
                        lcMenuList.visibility = View.VISIBLE
                        getMenuList()
                    } else {
                        lcMenuList.visibility = View.GONE
                    }
                }.apply { compositeDisposable.add(this) }

        rippleTest.clicks()
                .throttleFirst(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }.apply { compositeDisposable.add(this) }

    }


    override fun onAttachedToWindow() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        super.onAttachedToWindow()
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable = CompositeDisposable()
        setMenuButton()
        setSwipeView()
        (application as SlimeApplication).lockScreenShow = true
    }

    private fun setSwipeView() {
        lockScreenView.setOnTouchListener(mViewTouchListener)
    }

    private val mViewTouchListener = object : View.OnTouchListener {
        private var firstTouchX = 0f
        private var layoutPrevX = 0f
        private var lastLayoutX = 0f
        private var layoutInPrevX = 0f
        private var isLockOpen = false
        private var touchMoveX = 0
        private val touchInMoveX = 0

        override fun onTouch(v: View, event: MotionEvent): Boolean {

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    firstTouchX = event.x
                    layoutPrevX = lockScreenView.x
                    isLockOpen = true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isLockOpen) {
                        touchMoveX = (event.rawX - firstTouchX).toInt()
                        if (lockScreenView.x >= 0) {
                            lockScreenView.x = (layoutPrevX + touchMoveX).toInt().toFloat()
                            if (lockScreenView.x < 0) {
                                lockScreenView.x = 0f
                            }
                            lastLayoutX = lockScreenView.getX()
                        }
                    } else {
                        return false
                    }
                }
                MotionEvent.ACTION_UP -> { // 1
                    if (isLockOpen) {
                        lockScreenView.x = lastLayoutX
                        lockScreenView.y = 0f
                        optimizeForground(lastLayoutX)
                    }
                    isLockOpen = false
                    firstTouchX = 0f
                    layoutPrevX = 0f
                    layoutInPrevX = 0f
                    touchMoveX = 0
                    lastLayoutX = 0f
                }
                else -> {
                }
            }

            return true
        }
    }

    private fun optimizeForground(forgroundX: Float) {

        val displayMetrics = resources.displayMetrics
        val mDeviceWidth = displayMetrics.widthPixels
        val mDevideDeviceWidth = mDeviceWidth / 6

        if (forgroundX < mDevideDeviceWidth) {
            var startPostion = 0
            startPostion = mDevideDeviceWidth
            while (startPostion >= 0) {
                lockScreenView.x = startPostion.toFloat()
                startPostion--
            }
        } else {
            val animation = TranslateAnimation(0f, mDevideDeviceWidth.toFloat(), 0f, 0f)
            animation.duration = 300
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    lockScreenView.x = mDevideDeviceWidth.toFloat()
                    lockScreenView.y = 0f
                    finish()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })

            lockScreenView.startAnimation(animation)
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
        (application as SlimeApplication).lockScreenShow = false
    }

    override fun onBackPressed() {
        DLog.e("뒤로가기 클릭!!")
    }

}