package hanmo.com.slime.today

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import hanmo.com.slime.R
import hanmo.com.slime.constants.Type
import hanmo.com.slime.db.RealmHelper
import hanmo.com.slime.db.Slime
import hanmo.com.slime.model.SlimeData
import kotlinx.android.synthetic.main.activity_today.*

/**
 * Created by hanmo on 2018. 4. 7..
 */
class TodayActivity : AppCompatActivity() {

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, TodayActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today)

        val slimeData = ArrayList<SlimeData?>()

        with(todayList) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            getSlimeData(slimeData)
            val todayAdapter = TodayAdapter(slimeData, Type.LinearList)
            //todayAdapter.setOnItemClickListener(onItemClickListener)
            adapter = todayAdapter
        }
    }

    private fun getSlimeData(slimeData: ArrayList<SlimeData?>) {
        val slime = RealmHelper.instance.queryAll(Slime::class.java)
        slime?.forEach {
             slimeData.add(SlimeData(it.id, it.slimeName))
        }
    }
}