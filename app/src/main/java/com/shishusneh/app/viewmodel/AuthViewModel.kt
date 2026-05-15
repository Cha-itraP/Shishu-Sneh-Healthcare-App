package com.shishusneh.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shishusneh.app.data.auth.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.temporal.ChronoUnit

// ── Auth screens ──────────────────────────────────────────────────────────────
enum class AuthScreen { SPLASH, LOGIN, SIGNUP, APP }

// ── Auth UI State ─────────────────────────────────────────────────────────────
data class AuthUiState(
    val authScreen: AuthScreen = AuthScreen.SPLASH,

    // Login form
    val loginPhone: String = "",
    val loginPassword: String = "",
    val loginError: String = "",
    val loginPasswordVisible: Boolean = false,

    // Sign-up form — mother
    val signupMotherName: String = "",
    val signupMotherAge: String = "",
    val signupPhone: String = "",
    val signupPassword: String = "",
    val signupConfirmPassword: String = "",
    val signupPasswordVisible: Boolean = false,

    // Sign-up form — baby
    val signupBabyName: String = "",
    val signupBabyYear: String = "",
    val signupBabyMonth: String = "",
    val signupBabyDay: String = "",
    val signupBabyGender: String = "",   // "Girl" | "Boy" | "Other"

    // Step 1 = mother details, Step 2 = baby details
    val signupStep: Int = 1,
    val signupError: String = "",

    // Profile loaded from prefs (shown in app header / profile panel)
    val motherName: String = "",
    val babyName: String = "",
    val babyGender: String = "",
    val motherAge: String = ""
)

// ── ViewModel ─────────────────────────────────────────────────────────────────
class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = UserPreferences.getInstance(app)

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        // Splash → decide route
        if (prefs.isLoggedIn && prefs.isProfileComplete()) {
            _state.update {
                it.copy(
                    authScreen  = AuthScreen.APP,
                    motherName  = prefs.motherName,
                    babyName    = prefs.babyName,
                    babyGender  = prefs.babyGender,
                    motherAge   = prefs.motherAge
                )
            }
        } else {
            _state.update { it.copy(authScreen = AuthScreen.LOGIN) }
        }
    }

    // ── Login form ────────────────────────────────────────────────────────
    fun setLoginPhone(v: String)    = _state.update { it.copy(loginPhone = v, loginError = "") }
    fun setLoginPassword(v: String) = _state.update { it.copy(loginPassword = v, loginError = "") }
    fun toggleLoginPasswordVisible()= _state.update { it.copy(loginPasswordVisible = !it.loginPasswordVisible) }

    fun login() {
        val s = _state.value
        when {
            s.loginPhone.isBlank()    -> _state.update { it.copy(loginError = "Please enter your phone number") }
            s.loginPassword.isBlank() -> _state.update { it.copy(loginError = "Please enter your password") }
            // For a real app, verify against stored hash; here we check saved phone
            s.loginPhone != prefs.motherPhone ->
                _state.update { it.copy(loginError = "Phone number not found. Please sign up first.") }
            else -> {
                prefs.isLoggedIn = true
                _state.update {
                    it.copy(
                        authScreen  = AuthScreen.APP,
                        loginError  = "",
                        motherName  = prefs.motherName,
                        babyName    = prefs.babyName,
                        babyGender  = prefs.babyGender,
                        motherAge   = prefs.motherAge
                    )
                }
            }
        }
    }

    // ── Sign-up form — mother ─────────────────────────────────────────────
    fun setSignupMotherName(v: String)   = _state.update { it.copy(signupMotherName = v, signupError = "") }
    fun setSignupMotherAge(v: String)    = _state.update { it.copy(signupMotherAge = v, signupError = "") }
    fun setSignupPhone(v: String) {
        val digits = v.filter { it.isDigit() }.take(10)
        _state.update { it.copy(signupPhone = digits, signupError = "") }
    }
    fun setSignupPassword(v: String)     = _state.update { it.copy(signupPassword = v, signupError = "") }
    fun setSignupConfirmPassword(v: String) = _state.update { it.copy(signupConfirmPassword = v, signupError = "") }
    fun toggleSignupPasswordVisible()    = _state.update { it.copy(signupPasswordVisible = !it.signupPasswordVisible) }

    // ── Sign-up form — baby ───────────────────────────────────────────────
    fun setSignupBabyName(v: String)   = _state.update { it.copy(signupBabyName = v, signupError = "") }
    fun setSignupBabyYear(v: String)   = _state.update { it.copy(signupBabyYear = v, signupError = "") }
    fun setSignupBabyMonth(v: String)  = _state.update { it.copy(signupBabyMonth = v) }
    fun setSignupBabyDay(v: String)    = _state.update { it.copy(signupBabyDay = v) }
    fun setSignupBabyGender(v: String) = _state.update { it.copy(signupBabyGender = v, signupError = "") }

    /** Validate step 1 and advance to baby details */
    fun nextSignupStep() {
        val s = _state.value
        val age = s.signupMotherAge.toIntOrNull()
        when {
            s.signupMotherName.isBlank()  -> _state.update { it.copy(signupError = "Please enter your name") }
            s.signupMotherAge.isBlank() || age == null || age < 14 || age > 60
                -> _state.update { it.copy(signupError = "Please enter a valid age (14–60)") }
            s.signupPhone.length != 10    -> _state.update { it.copy(signupError = "Please enter exactly 10 digits") }
            s.signupPassword.length < 6   -> _state.update { it.copy(signupError = "Password must be at least 6 characters") }
            s.signupPassword != s.signupConfirmPassword
                -> _state.update { it.copy(signupError = "Passwords do not match") }
            else -> _state.update { it.copy(signupStep = 2, signupError = "") }
        }
    }

    fun backToSignupStep1() = _state.update { it.copy(signupStep = 1, signupError = "") }

    /** Validate step 2 and complete sign-up */
    fun completeSignup() {
        val s = _state.value
        val year = s.signupBabyYear.toIntOrNull()
        when {
            s.signupBabyName.isBlank()   -> _state.update { it.copy(signupError = "Please enter baby's name") }
            year == null || year < 2020 || year > 2026
                -> _state.update { it.copy(signupError = "Please enter a valid birth year (2020–2026)") }
            s.signupBabyGender.isBlank() -> _state.update { it.copy(signupError = "Please select baby's gender") }
            else -> {
                // Persist to SharedPreferences
                prefs.motherName  = s.signupMotherName.trim()
                prefs.motherAge   = s.signupMotherAge.trim()
                prefs.motherPhone = s.signupPhone.trim()
                prefs.motherEmail = ""
                prefs.babyName    = s.signupBabyName.trim()
                prefs.babyYearOfBirth  = s.signupBabyYear.trim()
                prefs.babyMonthOfBirth = s.signupBabyMonth.trim()
                prefs.babyDayOfBirth   = s.signupBabyDay.trim()
                prefs.babyGender  = s.signupBabyGender
                prefs.isLoggedIn  = true

                _state.update {
                    it.copy(
                        authScreen  = AuthScreen.APP,
                        signupError = "",
                        motherName  = prefs.motherName,
                        babyName    = prefs.babyName,
                        babyGender  = prefs.babyGender,
                        motherAge   = prefs.motherAge
                    )
                }
            }
        }
    }

    // ── Navigation ────────────────────────────────────────────────────────
    fun goToSignup() = _state.update { it.copy(authScreen = AuthScreen.SIGNUP, signupStep = 1, signupError = "", loginError = "") }
    fun goToLogin()  = _state.update { it.copy(authScreen = AuthScreen.LOGIN,  loginError = "") }

    fun logout() {
        prefs.clear()
        _state.update { AuthUiState(authScreen = AuthScreen.LOGIN) }
    }


    // ── Age calculation from DOB ──────────────────────────────────────────
    /**
     * Returns Pair(ageWeeks, ageDays) from stored DOB strings.
     * Falls back to (0, 0) if DOB is incomplete.
     */
    fun computeBabyAge(): Pair<Int, Int> {
        val year  = prefs.babyYearOfBirth.toIntOrNull()  ?: return 0 to 0
        val month = prefs.babyMonthOfBirth.toIntOrNull() ?: 1
        val day   = prefs.babyDayOfBirth.toIntOrNull()   ?: 1
        return try {
            val dob   = LocalDate.of(year, month, day)
            val today = LocalDate.now()
            val totalDays = ChronoUnit.DAYS.between(dob, today).toInt().coerceAtLeast(0)
            (totalDays / 7) to totalDays
        } catch (e: Exception) { 0 to 0 }
    }
}
