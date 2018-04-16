package hanmo.com.slime.lockscreen

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView

/**
 * Created by hanmo on 2018. 4. 16..
 */
class RemoveViewUtil private constructor() {

    fun unbindDrawables(view: View?) {
        if (null != view) {
            if (view.background != null) {
                view.background.callback = null
            }
            if (view is ViewGroup && view !is AdapterView<*>) {
                for (i in 0 until view.childCount) {
                    unbindDrawables(view.getChildAt(i))
                }
                view.removeAllViews()
            }
        }
    }

    companion object {
        private val TAG = "RemoveViewUtil"

        @Volatile
        private var RemoveViewUtilUniqueInstance: RemoveViewUtil? = null

        val instance: RemoveViewUtil?
            get() {
                if (RemoveViewUtilUniqueInstance == null) {
                    synchronized(RemoveViewUtil::class.java) {
                        if (RemoveViewUtilUniqueInstance == null) {
                            RemoveViewUtilUniqueInstance = RemoveViewUtil()
                        }
                    }
                }
                return RemoveViewUtilUniqueInstance
            }
    }
}

