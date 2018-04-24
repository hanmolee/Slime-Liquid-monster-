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
import hanmo.com.slime.lockscreen.utils.utils.Unlock


/**
 * Created by hanmo on 2018. 4. 18..
 */
class LockScreenActivity : AppCompatActivity() {

    private lateinit var compositeDisposable: CompositeDisposable

    private val onItemClickListener = object : LockScreenMenuAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            when(position) {
                0 -> {

                }
                1 -> { }
                2 -> { }
                3 -> { }
            }
        }

    }


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
            val menuAdapter = LockScreenMenuAdapter(menuList)
            menuAdapter.setOnItemClickListener(onItemClickListener)
            adapter = menuAdapter
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
        lockScreenView.setOnTouchListener(object : Unlock(this, lockScreenView) {
            override fun onFinish() {
                finish()
                super.onFinish()
            }
        })
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