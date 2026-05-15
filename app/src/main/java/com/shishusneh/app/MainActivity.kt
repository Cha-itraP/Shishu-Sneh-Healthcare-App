package com.shishusneh.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shishusneh.app.ui.components.NotificationsOverlay
import com.shishusneh.app.ui.screens.*
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AuthScreen
import com.shishusneh.app.viewmodel.AuthViewModel
import com.shishusneh.app.viewmodel.Screen
import com.shishusneh.app.viewmodel.ShishuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShishuSnehTheme {
                RootNavigation()
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Root — decides between Auth screens and App
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun RootNavigation() {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.state.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = authState.authScreen,
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        },
        label = "auth_nav"
    ) { screen ->
        when (screen) {
            AuthScreen.SPLASH -> SplashScreen()
            AuthScreen.LOGIN  -> LoginScreen(authState, authViewModel)
            AuthScreen.SIGNUP -> SignupScreen(authState, authViewModel)
            AuthScreen.APP    -> {
                val (ageWeeks, ageDays) = authViewModel.computeBabyAge()
                ShishuSnehApp(
                    motherName = authState.motherName,
                    babyName   = authState.babyName,
                    babyGender = authState.babyGender,
                    ageWeeks   = ageWeeks,
                    ageDays    = ageDays,
                    onLogout   = { authViewModel.logout() }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Splash screen (shown for ~instant while prefs are read)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SplashScreen() {
    Box(
        Modifier.fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFF7B4FD4), PurplePrimary, Color(0xFFE870FF)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("👶", fontSize = 56.sp)
            Spacer(Modifier.height(12.dp))
            Text("Shishu Sneh", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color.White)
            Text("Baby's First Year Guide", fontSize = 12.sp, color = Color.White.copy(0.8f))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Main App shell (now receives profile info from auth)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ShishuSnehApp(
    motherName: String,
    babyName: String,
    babyGender: String,
    ageWeeks: Int = 0,
    ageDays: Int = 0,
    onLogout: () -> Unit
) {
    val viewModel: ShishuViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Set real baby age from DOB on first composition
    LaunchedEffect(babyName, ageWeeks) {
        viewModel.setBabyProfile(babyName, ageWeeks, ageDays, babyGender)
    }

    Box(Modifier.fillMaxSize().background(AppBackground)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = AppBackground,
            bottomBar = { BottomNavBar(currentScreen = state.currentScreen) { viewModel.navigate(it) } }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                when (state.currentScreen) {
                    Screen.HOME       -> HomeScreen(state, viewModel)
                    Screen.GUIDE      -> GuideScreen(state, viewModel)
                    Screen.LOG        -> LogScreen(state, viewModel)
                    Screen.MILESTONES -> MilestonesScreen(state, viewModel)
                    Screen.HEALTH     -> HealthScreen(state, viewModel)
                }
            }
        }

        if (state.showNotifications) {
            NotificationsOverlay(state.notifications) { viewModel.toggleNotifications() }
        }

        // Profile panel — passes logout callback and real profile data
        ProfilePanel(
            state = state,
            viewModel = viewModel,
            motherName = motherName,
            babyName = babyName,
            babyGender = babyGender,
            onLogout = onLogout
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Bottom Navigation  (unchanged)
// ─────────────────────────────────────────────────────────────────────────────
private data class NavItem(val screen: Screen, val emoji: String, val label: String, val isFab: Boolean = false)

private val navItems = listOf(
    NavItem(Screen.HOME,       "🏠", "Home"),
    NavItem(Screen.GUIDE,      "📖", "Guide"),
    NavItem(Screen.LOG,        "➕", "Log", isFab = true),
    NavItem(Screen.MILESTONES, "🏆", "Milestone"),
    NavItem(Screen.HEALTH,     "🏥", "Health")
)

@Composable
fun BottomNavBar(currentScreen: Screen, onNavigate: (Screen) -> Unit) {
    Surface(tonalElevation = 8.dp, color = Color.White, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            navItems.forEach { item ->
                val active = currentScreen == item.screen
                if (item.isFab) {
                    Box(
                        modifier = Modifier.size(width = 52.dp, height = 52.dp).offset(y = (-12).dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Brush.linearGradient(listOf(PurpleLight, PurplePrimary)))
                            .clickable { onNavigate(item.screen) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(item.emoji, fontSize = 20.sp)
                            Text(item.label, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                            .background(if (active) PurpleBackground else Color.Transparent)
                            .clickable { onNavigate(item.screen) }
                            .padding(vertical = 5.dp, horizontal = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(item.emoji, fontSize = 17.sp)
                            Text(item.label, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold,
                                color = if (active) PurplePrimary else TextSecondary)
                        }
                    }
                }
            }
        }
    }
}
