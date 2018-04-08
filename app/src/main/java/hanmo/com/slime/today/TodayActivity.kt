package hanmo.com.slime.today

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import hanmo.com.slime.R
import kotlinx.android.synthetic.main.activity_today.*

/**
 * Created by hanmo on 2018. 4. 7..
 */
class TodayActivity : AppCompatActivity() {

    private val slimeNameArray = arrayOf("slime1", "slime2", "slime3", "slime4", "slime5", "slime6")

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, TodayActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today)

        with(todayList) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            val todayAdapter = TodayAdapter(slimeNameArray)
            //todayAdapter.setOnItemClickListener(onItemClickListener)
            adapter = todayAdapter
        }
    }
}