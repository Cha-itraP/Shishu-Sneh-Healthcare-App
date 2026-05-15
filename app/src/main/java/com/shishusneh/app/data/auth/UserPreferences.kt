package com.shishusneh.app.data.auth

import android.content.Context
import android.content.SharedPreferences

/**
 * Stores login state and user profile in SharedPreferences.
 * No sensitive data — just profile info + a "isLoggedIn" flag.
 */
class UserPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("shishu_user_prefs", Context.MODE_PRIVATE)

    // ── Auth ──────────────────────────────────────────────────────────────
    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_LOGGED_IN, false)
        set(v) = prefs.edit().putBoolean(KEY_LOGGED_IN, v).apply()

    // ── Mother profile ────────────────────────────────────────────────────
    var motherName: String
        get() = prefs.getString(KEY_MOTHER_NAME, "") ?: ""
        set(v) = prefs.edit().putString(KEY_MOTHER_NAME, v).apply()

    var motherAge: String
        get() = prefs.getString(KEY_MOTHER_AGE, "") ?: ""
        set(v) = prefs.edit().putString(KEY_MOTHER_AGE, v).apply()

    var motherEmail: String
        get() = prefs.getString(KEY_MOTHER_EMAIL, "") ?: ""
        set(v) = prefs.edit().putString(KEY_MOTHER_EMAIL, v).apply()

    var motherPhone: String
        get() = prefs.getString(KEY_MOTHER_PHONE, "") ?: ""
        set(v) = prefs.edit().putString(KEY_MOTHER_PHONE, v).apply()

    // ── Baby profile ──────────────────────────────────────────────────────
    var babyName: String
        get() = prefs.getString(KEY_BABY_NAME, "") ?: ""
        set(v) = prefs.edit().putString(KEY_BABY_NAME, v).apply()

    var babyYearOfBirth: String
        get() = prefs.getString(KEY_BABY_YEAR, "") ?: ""
        set(v) = prefs.edit().putString(KEY_BABY_YEAR, v).apply()

    var babyMonthOfBirth: String
        get() = prefs.getString(KEY_BABY_MONTH, "") ?: ""
        set(v) = prefs.edit().putString(KEY_BABY_MONTH, v).apply()

    var babyDayOfBirth: String
        get() = prefs.getString(KEY_BABY_DAY, "") ?: ""
        set(v) = prefs.edit().putString(KEY_BABY_DAY, v).apply()

    var babyGender: String
        get() = prefs.getString(KEY_BABY_GENDER, "") ?: ""
        set(v) = prefs.edit().putString(KEY_BABY_GENDER, v).apply()

    // ── Helpers ───────────────────────────────────────────────────────────
    /** Returns true if profile is fully filled. */
    fun isProfileComplete(): Boolean =
        motherName.isNotBlank() && babyName.isNotBlank() &&
        babyYearOfBirth.isNotBlank() && babyGender.isNotBlank()

    /** Clear everything — logs user out. */
    fun clear() = prefs.edit().clear().apply()

    companion object {
        private const val KEY_LOGGED_IN    = "logged_in"
        private const val KEY_MOTHER_NAME  = "mother_name"
        private const val KEY_MOTHER_AGE   = "mother_age"
        private const val KEY_MOTHER_EMAIL = "mother_email"
        private const val KEY_MOTHER_PHONE = "mother_phone"
        private const val KEY_BABY_NAME    = "baby_name"
        private const val KEY_BABY_YEAR    = "baby_year"
        private const val KEY_BABY_MONTH   = "baby_month"
        private const val KEY_BABY_DAY     = "baby_day"
        private const val KEY_BABY_GENDER  = "baby_gender"

        @Volatile private var INSTANCE: UserPreferences? = null
        fun getInstance(context: Context): UserPreferences =
            INSTANCE ?: synchronized(this) {
                UserPreferences(context.applicationContext).also { INSTANCE = it }
            }
    }
}
