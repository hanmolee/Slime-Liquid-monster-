package hanmo.com.slime.lockscreen.utils.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import hanmo.com.slime.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_changebackground.*

/**
 * Created by hanmo on 2018. 4. 24..
 */
class ChangeBackgroundActivity : AppCompatActivity() {

    private lateinit var compositeDisposable: CompositeDisposable

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, ChangeBackgroundActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changebackground)

        compositeDisposable = CompositeDisposable()

        setCancelButton()
        setBackgroundList()
    }

    private fun setCancelButton() {
        cancelButton.clicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    finish()
                }
                .apply { compositeDisposable.add(this) }
    }

    private fun setBackgroundList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}