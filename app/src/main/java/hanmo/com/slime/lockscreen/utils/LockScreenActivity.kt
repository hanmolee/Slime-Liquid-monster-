package hanmo.com.slime.lockscreen.utils

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import hanmo.com.slime.R
import hanmo.com.slime.SlimeApplication
import hanmo.com.slime.util.DLog
import hanmo.com.slime.util.RippleViewCreator
import kotlinx.android.synthetic.main.activity_lockscreen.*
import com.gordonwong.materialsheetfab.MaterialSheetFab




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

        hanmo.setOnClickListener {
            val menuList = arrayOf("배경사진 바꾸기", "사용방법 다시보기", "문의하기", "설정")
            val spinnerAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, menuList)
            spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            lockscreenMenu.adapter = spinnerAdapter
        }


        RippleViewCreator.addRippleToView(rippleTest)

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