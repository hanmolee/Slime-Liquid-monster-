package hanmo.com.slime.lockscreen

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

/**
 * Created by hanmo on 2018. 4. 16..
 */
class LockScreenView : RelativeLayout {
    private var mActivityContext: Context? = null
    private val mLayoutView: View? = null

    constructor(context: Context) : super(context) {
        mActivityContext = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mActivityContext = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mActivityContext = context
        initView()
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        mActivityContext = context
        initView()
    }

    private fun initView() {

    }

}
