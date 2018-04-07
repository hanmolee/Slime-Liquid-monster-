package hanmo.com.slime.search

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import hanmo.com.slime.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Created by hanmo on 2018. 4. 7..
 */
class SearchActivity : AppCompatActivity() {

    private lateinit var compositeDisposable: CompositeDisposable
    private var isEditTextVisible: Boolean = false
    private lateinit var inputManager: InputMethodManager

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, SearchActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        compositeDisposable = CompositeDisposable()
        inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setSearchButton()
    }

    private fun setSearchButton() {
        frame_layout.visibility = View.INVISIBLE

        btn_search.clicks()
                .doOnNext {
                    if (!isEditTextVisible){
                        revealEditText(frame_layout)
                    }
                    else {
                        hideEditText(frame_layout)
                    }
                }
                .subscribe {
                    Toast.makeText(applicationContext, "click", Toast.LENGTH_SHORT).show()
                }
                .apply { compositeDisposable.add(this) }
    }

    private fun revealEditText(view: ConstraintLayout) {
        val cx = btn_search.right - 30
        val cy = btn_search.bottom - 60
        val finalRadius = Math.max(view.width, view.height)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0F, finalRadius.toFloat())
        view.visibility = View.VISIBLE
        isEditTextVisible = true
        anim.start()

        et_search.text.clear()
        et_search.requestFocus()
        inputManager.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT)

        btn_search.setImageResource(R.drawable.ic_confirm)
        val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
        alphaAnimation.duration = 200
        btn_search.startAnimation(alphaAnimation)
    }

    private fun hideEditText(view: ConstraintLayout) {
        val cx = btn_search.right - 30
        val cy = btn_search.bottom - 60
        val initialRadius = view.width
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.INVISIBLE
            }
        })
        isEditTextVisible = false
        anim.start()

        inputManager.hideSoftInputFromWindow(et_search.windowToken, 0)

        btn_search.setImageResource(R.drawable.ic_search)
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 200
        btn_search.startAnimation(alphaAnimation)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}