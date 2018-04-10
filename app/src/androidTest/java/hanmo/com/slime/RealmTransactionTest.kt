package hanmo.com.slime

import android.support.test.InstrumentationRegistry
import hanmo.com.slime.db.FavoriteSlime
import hanmo.com.slime.db.SearchHistory
import hanmo.com.slime.db.Slime
import io.realm.Realm
import io.realm.RealmResults
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

    @Test
    fun f_insertSlime() {
        val slimeMaxId = realm.where(Slime::class.java).max("id")
        val nextId : Int =
                when(slimeMaxId) {
                    null -> { 1 }
                    else -> { slimeMaxId.toInt() + 1 }
                }

        val slimeName = "slime1"

        val slime = Slime()
        slime.id = nextId
        slime.slimeName = slimeName

        realm.executeTransaction {
            it.copyToRealm(slime)
        }

        val checkSlime = realm.where(Slime::class.java).equalTo("id", nextId).findFirst()
        assertNotNull(checkSlime)

        checkSlime?.let {
            assertEquals(nextId, it.id)
            assertEquals(slimeName, it.slimeName)
        }

    }

    @Test
    fun g_searchSlimeData() {

        val searchSlime = "slime1"
        val slimeData = realm.where(Slime::class.java).contains("slimeName", searchSlime).findAll()
        assertNotNull(slimeData)

    }

    @Test
    fun h_getSearchSlimeData() {
        val getSearchHistory = realm.where(SearchHistory::class.java).findAll().sort("searchTime", Sort.DESCENDING).first()

        val slimeData = realm.where(Slime::class.java).contains("slimeName", getSearchHistory?.historyName).findAll()
        assertNotNull(slimeData)

    }

    @Test
    fun i_insertFavoriteData() {
        val favoriteMaxId = realm.where(FavoriteSlime::class.java).max("id")
        val nextId : Int =
                when(favoriteMaxId) {
                    null -> { 1 }
                    else -> { favoriteMaxId.toInt() + 1 }
                }

        val slimeName = "slime1"

        val favorite = FavoriteSlime()
        favorite.id = nextId
        favorite.name = slimeName

        realm.executeTransaction {
            it.copyToRealm(favorite)
        }

        val checkFavorite = realm.where(FavoriteSlime::class.java).equalTo("id", nextId).findFirst()
        assertNotNull(checkFavorite)

        checkFavorite?.let {
            assertEquals(nextId, it.id)
            assertEquals(slimeName, it.name)
        }
    }

    @Test
    fun j_deleteFavoriteData() {
        val favoriteName = "slime1"
        val favorite = realm.where(FavoriteSlime::class.java).equalTo("name", favoriteName).findFirst()

        assertNotNull(favorite)

        realm.executeTransaction {
            favorite?.deleteFromRealm()
        }

        val checkFavorite = realm.where(FavoriteSlime::class.java).equalTo("name", favoriteName).findFirst()
        assertNull(checkFavorite)
    }

    @After
    fun closeDB() {
        if (!realm.isClosed) {
            realm.close()
        }
    }
}