package hanmo.com.slime.lockscreen.utils

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import hanmo.com.slime.R
import hanmo.com.slime.SlimeApplication
import hanmo.com.slime.lockscreen.utils.utils.LockScreenService
import hanmo.com.slime.util.DLog
import android.os.Looper.loop
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_lockscreen.*


/**
 * Created by hanmo on 2018. 4. 18..
 */
class LockScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lockscreen)

        /*with(pigLottie) {
            imageAssetsFolder = "images/"
            setAnimation("pig.json")
            loop(true)
            playAnimation()
        }*/


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