package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.AuthUiState
import com.shishusneh.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    state: AuthUiState,
    viewModel: AuthViewModel
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero header ──────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF7B4FD4), PurplePrimary, Color(0xFFE870FF))
                        )
                    )
                    .padding(top = 60.dp, bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // App logo circle
                    Box(
                        Modifier
                            .size(80.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            .border(2.dp, Color.White.copy(0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text("👶", fontSize = 38.sp) }

                    Spacer(Modifier.height(14.dp))
                    Text("Shishu Sneh", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Text("Baby's First Year Guide", fontSize = 12.sp, color = Color.White.copy(0.85f))
                }
            }

            // ── Login card ───────────────────────────────────────────────
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp)
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("Welcome back 🌸", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text("Sign in to continue tracking your baby's journey", fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)

                        // Phone
                        AuthTextField(
                            value = state.loginPhone,
                            onValueChange = { viewModel.setLoginPhone(it) },
                            label = "Phone Number",
                            placeholder = "Enter your phone number",
                            emoji = "📱",
                            keyboardType = KeyboardType.Phone
                        )

                        // Password
                        AuthTextField(
                            value = state.loginPassword,
                            onValueChange = { viewModel.setLoginPassword(it) },
                            label = "Password",
                            placeholder = "Enter your password",
                            emoji = "🔒",
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            passwordVisible = state.loginPasswordVisible,
                            onTogglePassword = { viewModel.toggleLoginPasswordVisible() }
                        )

                        // Error
                        if (state.loginError.isNotBlank()) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFEEEE), RoundedCornerShape(10.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⚠️", fontSize = 14.sp)
                                Text(state.loginError, fontSize = 10.sp, color = Color(0xFFCC2020), fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Login button
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(listOf(PurpleLight, PurplePrimary)),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { viewModel.login() }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Sign In", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Sign up prompt
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("New here? ", fontSize = 12.sp, color = TextSecondary)
                    Text(
                        "Create an account",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurplePrimary,
                        modifier = Modifier.clickable { viewModel.goToSignup() }
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Demo tip
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF8E8), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("💡", fontSize = 14.sp)
                    Text(
                        "First time? Tap 'Create an account' above to register your details and baby's profile.",
                        fontSize = 9.sp, color = Color(0xFF885500), lineHeight = 13.sp
                    )
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
