package hanmo.com.slime.lockscreen.utils

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import hanmo.com.slime.R
import hanmo.com.slime.SlimeApplication
import hanmo.com.slime.util.DLog
import hanmo.com.slime.util.RippleViewCreator
import kotlinx.android.synthetic.main.activity_lockscreen.*
import com.gordonwong.materialsheetfab.MaterialSheetFab
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit


/**
 * Created by hanmo on 2018. 4. 18..
 */
class LockScreenActivity : AppCompatActivity() {

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lockscreen)

        /*with(pigLottie) {
            imageAssetsFolder = "images/"
            setAnimation("pig.json")
            loop(true)
            playAnimation()
        }*/

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

        RxView.clicks(lockscreenMenuButton)
                .throttleFirst(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    if (lcMenuList.visibility == View.GONE){
                        lcMenuList.visibility = View.VISIBLE
                        getMenuList()
                    } else {
                        lcMenuList.visibility = View.GONE
                    }
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
        (application as SlimeApplication).lockScreenShow = true
    }


    override fun onPause() {
        super.onPause()
        (application as SlimeApplication).lockScreenShow = false
    }

    override fun onBackPressed() {
        DLog.e("뒤로가기 클릭!!")
    }
}