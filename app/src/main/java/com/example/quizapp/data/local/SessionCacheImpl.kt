package com.example.quizapp.data.local

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

class SessionCacheImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SessionCache {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val adapter = moshi.adapter(Session::class.java)

    override fun saveSession(session: Session) {
        sharedPreferences.edit()
            .putString("local", adapter.toJson(session))
            .apply()
    }

    override fun getActiveSession(): Session? {
        val json = sharedPreferences.getString("local", null) ?: return null
        return adapter.fromJson(json)
    }

    override fun clearSession() {
        sharedPreferences.edit().remove("local").apply()
    }
}