package hanmo.com.slime.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.HashSet

/**
 * Created by hanmo on 2018. 4. 12..
 */
class SharedPreferencesUtil(val context: Context) {
    private val PREF_NAME = "slime"

    private lateinit var mContext: Context
    private val TAG = "SPreference"
    private var pref: SharedPreferences? = null

    init {
        if (context == null) {
            throw NullPointerException("Context must be not null!")
        } else {
            mContext = context
        }
    }

    fun remove(key: String) {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        val editor = pref!!.edit()

        editor.remove(key)
        editor.apply()
    }

    /**
     * sharedpreference 에 키가있으면 true 아니면 false
     *
     * @param key
     * @return
     */
    fun has(key: String): Boolean {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        val editor = pref!!.edit()
        return pref!!.all.containsKey(key)
    }

    fun put(key: String, value: String) {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        val editor = pref!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun put(key: String, value: Set<String>) {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        val editor = pref!!.edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    fun put(key: String, value: Float) {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        val editor = pref!!.edit()
        //        Log.e(TAG, "put float key : " + key + " / put float value : " + value);
        editor.putFloat(key, value)
        editor.apply()
    }

    fun put(key: String, value: Boolean) {
        val pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putBoolean(key, value)
        editor.apply()
    }

    fun put(key: String, dftValue: Int) {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        val editor = pref!!.edit()
        //        Log.e(TAG, "put int key : " + key + " / put int value : " + pref.getInt(key, dftValue));
        editor.putInt(key, dftValue)
        editor.apply()
    }

    fun getFloat(key: String, dftValue: Float): Float? {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)

        //        Log.e(TAG, "get float key : " + key + " / get float value : " + pref.getFloat(key, dftValue));
        return pref?.getFloat(key, dftValue)
    }

    fun getHashSet(key: String, dftValue: HashSet<String>): Set<String>? {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)

        //        Log.e(TAG, "get float key : " + key + " / get float value : " + pref.getFloat(key, dftValue));
        return pref?.getStringSet(key, dftValue)
    }

    fun getString(key: String): String? {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)

        return pref?.getString(key, "")
    }

    fun getString(key: String, dftValue: String): String? {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)

        return pref?.getString(key, dftValue)
    }

    fun getInt(key: String, dftValue: Int): Int? {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        return pref?.getInt(key, dftValue)
    }

    fun getBool(key: String, dftValue: Boolean): Boolean? {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)

        return try {
            pref?.getBoolean(key, dftValue)
        } catch (e: Exception) {
            dftValue
        }

    }

    fun getAllValue(): String {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        return try {
            pref?.all.toString()
        } catch (e: Exception) {
            "no Preference Data"
        }

    }

    /**
     * remove all key, value
     */
    fun clear() {
        pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.let {
            it.clear()
            it.apply()
        }
    }
}
