package hanmo.com.slime

import android.support.test.InstrumentationRegistry
import hanmo.com.slime.db.SearchHistory
import io.realm.Realm
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by hanmo on 2018. 4. 7..
 */
@RunWith(JUnit4::class)
class RealmTransactionTest {

    private lateinit var realm : Realm

    @Before
    fun initDB() {
        Realm.init(InstrumentationRegistry.getTargetContext())
        realm = Realm.getDefaultInstance()
    }

    @Test
    fun 검색어를_입력해서_버튼을누르면_히스토리에_저장되는가() {

        val historyMaxId = realm.where(SearchHistory::class.java).max("id")
        val nextId : Int =
                when(historyMaxId) {
                    null -> { 1 }
                    else -> { historyMaxId.toInt() + 1 }
                }
        val currentTime = System.currentTimeMillis()
        val historyName = "Search Slime"

        val history = SearchHistory()
        history.id = nextId
        history.historyName = historyName
        history.searchTime = currentTime

        realm.executeTransaction {
            it.copyToRealm(history)
        }

        val checkHistory = realm.where(SearchHistory::class.java).equalTo("id", nextId).findFirst()
        assertNotNull(checkHistory)

        checkHistory?.let {
            assertEquals(nextId, it.id)
            assertEquals(historyName, it.historyName)
            assertEquals(currentTime, it.searchTime)
        }

    }

    @After
    fun closeDB() {
        if (!realm.isClosed) {
            realm.close()
        }
    }
}