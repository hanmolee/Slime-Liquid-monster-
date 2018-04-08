package hanmo.com.slime

import android.support.test.InstrumentationRegistry
import hanmo.com.slime.db.SearchHistory
import io.realm.Realm
import io.realm.Sort
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.MethodSorters

/**
 * Created by hanmo on 2018. 4. 7..
 */
@RunWith(JUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RealmTransactionTest {

    private lateinit var realm : Realm

    @Before
    fun initDB() {
        Realm.init(InstrumentationRegistry.getTargetContext())
        realm = Realm.getDefaultInstance()
    }

    @Test
    fun a검색어를_입력해서_버튼을누르면_히스토리에_저장되는가() {

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

    @Test
    fun bselectSearchHistory() {

        val selectQuery = realm.where(SearchHistory::class.java).distinct("historyName").findAll().sort("searchTime", Sort.DESCENDING)
        assertNotNull(selectQuery)
        assertNotEquals(true, selectQuery.isEmpty())
    }

    @Test
    fun c검색히스토리_클릭시_가장최신으로_검색어가_위치하는가() {

        val searchItemName = "Search Slime"
        val history = realm.where(SearchHistory::class.java).equalTo("historyName", searchItemName).findFirst()

        val currentTime = System.currentTimeMillis()

        assertNotNull(history)

        realm.executeTransaction {
            history?.let {
                it.searchTime = currentTime
            }
        }

        val checkHistory = realm.where(SearchHistory::class.java).equalTo("historyName", searchItemName).findFirst()
        assertNotNull(checkHistory)

        checkHistory?.let {
            assertEquals(currentTime, it.searchTime)
        }

    }

    @Test
    fun d검색히스토리가_개별적으로_삭제되는가() {
        val searchItemName = "Search Slime"
        val history = realm.where(SearchHistory::class.java).equalTo("historyName", searchItemName).findFirst()

        assertNotNull(history)

        realm.executeTransaction {
            history?.deleteFromRealm()
        }

        val checkHistory = realm.where(SearchHistory::class.java).equalTo("historyName", searchItemName).findFirst()
        assertNull(checkHistory)
    }

    @Test
    fun e모든검색히스토리가_삭제되는가() {

        for (i in 1..5) {
            val history = SearchHistory()
            history.id = i
            history.historyName = "historyName Number is $i"
            history.searchTime = System.currentTimeMillis()

            realm.executeTransaction {
                it.copyToRealm(history)
            }
        }

        val historys = realm.where(SearchHistory::class.java).findAll()

        assertNotNull(historys)
        assertEquals(false, historys.isEmpty())

        realm.executeTransaction {
            historys?.deleteAllFromRealm()
        }

        val checkHistory = realm.where(SearchHistory::class.java).findAll()
        assertNotNull(checkHistory)
        assertEquals(true, checkHistory.isEmpty())
    }

    @After
    fun closeDB() {
        if (!realm.isClosed) {
            realm.close()
        }
    }
}