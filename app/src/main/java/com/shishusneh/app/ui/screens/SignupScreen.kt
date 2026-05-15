package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun SignupScreen(
    state: AuthUiState,
    viewModel: AuthViewModel
) {
    Box(Modifier.fillMaxSize().background(AppBackground)) {
        Column(Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Color(0xFF7B4FD4), PurplePrimary, Color(0xFFE870FF))))
                    .padding(top = 44.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (state.signupStep == 2) viewModel.backToSignupStep1()
                                else viewModel.goToLogin()
                            },
                            modifier = Modifier.size(34.dp).background(Color.White.copy(0.2f), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (state.signupStep == 1) "Create Account" else "Baby's Details",
                                fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White
                            )
                            Text(
                                "Step ${state.signupStep} of 2",
                                fontSize = 10.sp, color = Color.White.copy(0.8f)
                            )
                        }
                        Spacer(Modifier.size(34.dp))
                    }

                    Spacer(Modifier.height(14.dp))

                    // Step progress bar
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(1, 2).forEach { step ->
                            Box(
                                Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .background(
                                        if (step <= state.signupStep) Color.White
                                        else Color.White.copy(0.3f),
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                }
            }

            // ── Form ──────────────────────────────────────────────────────
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (state.signupStep == 1) {
                    MotherDetailsForm(state, viewModel)
                } else {
                    BabyDetailsForm(state, viewModel)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Step 1 — Mother details
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun MotherDetailsForm(state: AuthUiState, viewModel: AuthViewModel) {

    SectionHeader("About You 👩", "Tell us about yourself so we can personalise your experience")

    AuthTextField(
        value = state.signupMotherName,
        onValueChange = { viewModel.setSignupMotherName(it) },
        label = "Mother's Full Name *",
        placeholder = "e.g. Priya Sharma",
        emoji = "👩"
    )

    AuthTextField(
        value = state.signupMotherAge,
        onValueChange = { viewModel.setSignupMotherAge(it) },
        label = "Mother's Age *",
        placeholder = "e.g. 28",
        emoji = "🎂",
        keyboardType = KeyboardType.Number
    )

    // Phone field — digits only, max 10
    OutlinedTextField(
        value = state.signupPhone,
        onValueChange = { v ->
            val digits = v.filter { it.isDigit() }.take(10)
            viewModel.setSignupPhone(digits)
        },
        label = { Text("Phone Number *  (${state.signupPhone.length}/10)", fontSize = 11.sp,
            color = if (state.signupPhone.length == 10) Color(0xFF1A7A4A) else TextSecondary) },
        placeholder = { Text("10-digit mobile number", fontSize = 11.sp, color = TextSecondary) },
        leadingIcon = { Text("📱", fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (state.signupPhone.length == 10) Color(0xFF1A7A4A) else PurplePrimary,
            unfocusedBorderColor = if (state.signupPhone.length == 10) Color(0xFF1A7A4A) else Divider,
            focusedLabelColor = PurplePrimary
        )
    )

    AuthTextField(
        value = state.signupPassword,
        onValueChange = { viewModel.setSignupPassword(it) },
        label = "Create Password *",
        placeholder = "At least 6 characters",
        emoji = "🔒",
        keyboardType = KeyboardType.Password,
        isPassword = true,
        passwordVisible = state.signupPasswordVisible,
        onTogglePassword = { viewModel.toggleSignupPasswordVisible() }
    )

    AuthTextField(
        value = state.signupConfirmPassword,
        onValueChange = { viewModel.setSignupConfirmPassword(it) },
        label = "Confirm Password *",
        placeholder = "Re-enter your password",
        emoji = "🔐",
        keyboardType = KeyboardType.Password,
        isPassword = true,
        passwordVisible = state.signupPasswordVisible,
        onTogglePassword = { viewModel.toggleSignupPasswordVisible() }
    )

    // Error
    SignupError(state.signupError)

    // Next button
    PrimaryButton("Next — Baby Details →") { viewModel.nextSignupStep() }

    // Already have account
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text("Already have an account? ", fontSize = 11.sp, color = TextSecondary)
        Text("Sign In", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary,
            modifier = Modifier.clickable { viewModel.goToLogin() })
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Step 2 — Baby details
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun BabyDetailsForm(state: AuthUiState, viewModel: AuthViewModel) {

    SectionHeader("Your Baby 👶", "Help us set up the right milestones and tips for your little one")

    AuthTextField(
        value = state.signupBabyName,
        onValueChange = { viewModel.setSignupBabyName(it) },
        label = "Baby's Name *",
        placeholder = "e.g. Krishna",
        emoji = "👶"
    )

    // Date of birth row
    Text("Date of Birth *", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // Day
        OutlinedTextField(
            value = state.signupBabyDay,
            onValueChange = { if (it.length <= 2) viewModel.setSignupBabyDay(it) },
            label = { Text("Day", fontSize = 10.sp) },
            placeholder = { Text("DD", fontSize = 10.sp, color = TextSecondary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = authFieldColors()
        )
        // Month dropdown replaced with simple text field
        OutlinedTextField(
            value = state.signupBabyMonth,
            onValueChange = { if (it.length <= 2) viewModel.setSignupBabyMonth(it) },
            label = { Text("Month", fontSize = 10.sp) },
            placeholder = { Text("MM", fontSize = 10.sp, color = TextSecondary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = authFieldColors()
        )
        // Year
        OutlinedTextField(
            value = state.signupBabyYear,
            onValueChange = { if (it.length <= 4) viewModel.setSignupBabyYear(it) },
            label = { Text("Year", fontSize = 10.sp) },
            placeholder = { Text("YYYY", fontSize = 10.sp, color = TextSecondary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1.3f),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = authFieldColors()
        )
    }

    // Gender selector
    Text("Baby's Gender *", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        listOf("👧 Girl", "👦 Boy", "🌈 Other").forEach { option ->
            val key = option.substringAfter(" ")  // "Girl", "Boy", "Other"
            val active = state.signupBabyGender == key
            Box(
                Modifier
                    .weight(1f)
                    .background(
                        if (active) Brush.linearGradient(listOf(PurpleLight, PurplePrimary))
                        else Brush.linearGradient(listOf(Color.White, Color.White)),
                        RoundedCornerShape(14.dp)
                    )
                    .border(2.dp, if (active) Color.Transparent else Divider, RoundedCornerShape(14.dp))
                    .clickable { viewModel.setSignupBabyGender(key) }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(option.substringBefore(" "), fontSize = 22.sp)
                    Text(key, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (active) Color.White else TextSecondary)
                }
            }
        }
    }

    // Summary preview card
    if (state.signupMotherName.isNotBlank()) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = PurpleBackground),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("✅ Profile Summary", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary)
                Spacer(Modifier.height(2.dp))
                SummaryLine("Mother", state.signupMotherName + if (state.signupMotherAge.isNotBlank()) "  ·  Age ${state.signupMotherAge}" else "")
                if (state.signupBabyName.isNotBlank()) SummaryLine("Baby", state.signupBabyName)
                if (state.signupBabyYear.isNotBlank()) SummaryLine("Born", "${state.signupBabyDay.ifBlank{"?"}}.${state.signupBabyMonth.ifBlank{"?"}}.${state.signupBabyYear}")
                if (state.signupBabyGender.isNotBlank()) SummaryLine("Gender", state.signupBabyGender)
            }
        }
    }

    // Error
    SignupError(state.signupError)

    // Complete button
    PrimaryButton("Complete Sign Up 🎉") { viewModel.completeSignup() }

    Spacer(Modifier.height(8.dp))
}

// ─────────────────────────────────────────────────────────────────────────────
//  Shared sub-composables
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    emoji: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.sp) },
        placeholder = { Text(placeholder, fontSize = 11.sp, color = TextSecondary) },
        leadingIcon = { Text(emoji, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp)) },
        trailingIcon = if (isPassword && onTogglePassword != null) {{
            IconButton(onClick = onTogglePassword) {
                Icon(
                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    null, tint = TextSecondary, modifier = Modifier.size(18.dp)
                )
            }
        }} else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = authFieldColors()
    )
}

@Composable
fun authFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor  = PurplePrimary,
    unfocusedBorderColor = Divider,
    focusedLabelColor   = PurplePrimary
)

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Column {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
        Text(subtitle, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)
    }
}

@Composable
fun SignupError(error: String) {
    if (error.isNotBlank()) {
        Row(
            Modifier.fillMaxWidth()
                .background(Color(0xFFFFEEEE), RoundedCornerShape(10.dp))
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⚠️", fontSize = 14.sp)
            Text(error, fontSize = 10.sp, color = Color(0xFFCC2020), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun PrimaryButton(label: String, onClick: () -> Unit) {
    Box(
        Modifier.fillMaxWidth()
            .background(Brush.linearGradient(listOf(PurpleLight, PurplePrimary)), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
    }
}

@Composable
fun SummaryLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontSize = 9.sp, color = TextSecondary, modifier = Modifier.width(48.dp))
        Text(value, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
    }
}
