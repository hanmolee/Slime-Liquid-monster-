package hanmo.com.slime.db

import android.util.Log
import io.realm.*

/**
 * Created by hanmo on 2018. 4. 8..
 */
class RealmHelper {

    var realm: Realm
        private set

    init {
        realm = try {

            Realm.getDefaultInstance()

        } catch (e: Exception) {

            Log.d("Realm Exception", e.toString())

            val config = RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build()
            Realm.getInstance(config)
        }
    }

    fun insertSlimeSampleData(slimeName : String) {
        val slimeMaxId = realm.where(Slime::class.java).max("id")
        val nextId : Int =
                when(slimeMaxId) {
                    null -> { 1 }
                    else -> { slimeMaxId.toInt() + 1 }
                }

        val slime = Slime()
        slime.id = nextId
        slime.slimeName = slimeName

        addData(slime)

    }

    fun selectSLimeSampleData(searchData : String?): RealmResults<Slime>? {
        return realm.where(Slime::class.java).contains("slimeName", searchData).findAll()
    }

    fun insertSearchHistory(historyName : String) {

        val historyMaxId = realm.where(SearchHistory::class.java).max("id")
        val nextId : Int =
                when(historyMaxId) {
                    null -> { 1 }
                    else -> { historyMaxId.toInt() + 1 }
                }

        val history = SearchHistory()
        history.id = nextId
        history.historyName = historyName
        history.searchTime = System.currentTimeMillis()

        addData(history)
    }

    fun selectSearchHistory() : RealmResults<SearchHistory>? {

        return realm.where(SearchHistory::class.java).distinct("historyName").findAll().sort("searchTime", Sort.DESCENDING)
    }

    fun getSearchSlimeData(): RealmResults<Slime>? {
        val getSearchHistory = realm.where(SearchHistory::class.java).findAll().sort("searchTime", Sort.DESCENDING).first()

        return selectSLimeSampleData(getSearchHistory?.historyName)
    }


    //Insert To Realm
    fun <T : RealmObject> addData(data: T) {
        realm.executeTransaction {
            realm.copyToRealm(data)
        }
    }

    //Insert To Realm with RealmList
    fun <T : RealmObject> addRealmListData(data: T) {
        realm.executeTransaction {
            realm.copyToRealmOrUpdate(data)
        }
    }

    fun <T : RealmObject> queryAll(clazz: Class<T>): RealmResults<T>? {
        return realm.where(clazz).findAll()
    }

    companion object {

        private var INSTANCE: RealmHelper? = RealmHelper()

        val instance: RealmHelper
            get() {
                if (INSTANCE == null) {
                    INSTANCE = RealmHelper()
                }
                return INSTANCE as RealmHelper
            }
    }

}

