package hanmo.com.slime.lockscreen

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity

/**
 * Created by hanmo on 2018. 4. 16..
 */
class PermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivityForResult(intent, 123)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            123 -> {
                if (Settings.canDrawOverlays(this)) {
                    LockscreenUtil.getInstance(this).getPermissionCheckSubject()?.onNext(true)
                    finish()
                }
            }
        }
    }
}