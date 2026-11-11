package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.network.AccountApiException
import com.example.network.AccountProfileDto
import com.example.network.AccountRepository
import kotlinx.coroutines.launch

private enum class AccessPage {
    WELCOME,
    LOGIN,
    REGISTER,
    VERIFY_EMAIL,
    RESET_REQUEST,
    RESET_CONFIRM
}

@Composable
fun AccountAccessScreen(
    repository: AccountRepository,
    onAuthenticated: (AccountProfileDto) -> Unit
) {
    var page by remember { mutableStateOf(AccessPage.WELCOME) }

    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    fun clearFeedback() {
        error = null
        message = null
    }

    fun navigate(target: AccessPage) {
        clearFeedback()
        page = target
    }

    fun runAction(action: suspend () -> Unit) {
        if (busy) return

        scope.launch {
            busy = true
            clearFeedback()

            try {
                action()
            } catch (exception: AccountApiException) {
                error = exception.message
            } catch (_: Exception) {
                error = "Unable to complete the request. Check your connection and try again."
            } finally {
                busy = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(18.dp))

        Text(
            text = "SCI IDEA",
            fontSize = 29.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF166534)
        )

        Text(
            text = "Discover. Plan. Build.",
            fontSize = 13.sp,
            color = Color(0xFF64748B)
        )

        Spacer(Modifier.height(26.dp))

        when (page) {

            AccessPage.WELCOME -> {

                Image(
                    painter = painterResource(R.drawable.welcome_portrait),
                    contentDescription = "SCI IDEA welcome portrait",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )

                Spacer(Modifier.height(25.dp))

                Text(
                    text = "Turn Your Ideas Into Reality",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Discover business opportunities, explore your potential and take your first step towards entrepreneurship.",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(28.dp))

                Button(
                    onClick = { navigate(AccessPage.REGISTER) },
                    enabled = !busy,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF166534)
                    )
                ) {
                    Text(
                        "Get Started",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { navigate(AccessPage.LOGIN) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Log In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            AccessPage.LOGIN -> {

                AccessHeading(
                    title = "Welcome Back",
                    subtitle = "Sign in to continue your entrepreneurial journey."
                )

                AccessField(
                    label = "Email address or phone number",
                    value = identifier,
                    onValueChange = { identifier = it },
                    keyboardType = KeyboardType.Text
                )

                PasswordField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it }
                )

                TextButton(
                    onClick = { navigate(AccessPage.RESET_REQUEST) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot password?")
                }

                AccessButton(
                    text = "Log In",
                    busy = busy
                ) {
                    runAction {
                        val account = repository.login(
                            identifier,
                            password
                        )

                        password = ""
                        onAuthenticated(account)
                    }
                }

                TextButton(
                    onClick = { navigate(AccessPage.REGISTER) }
                ) {
                    Text("Don't have an account? Create one")
                }

                TextButton(
                    onClick = { navigate(AccessPage.VERIFY_EMAIL) }
                ) {
                    Text("Already registered? Verify your email")
                }

                BackToWelcome {
                    navigate(AccessPage.WELCOME)
                }
            }

            AccessPage.REGISTER -> {

                AccessHeading(
                    title = "Create Your Account",
                    subtitle = "Join SCI IDEA and start exploring business opportunities."
                )

                AccessField(
                    label = "First name",
                    value = firstName,
                    onValueChange = { firstName = it }
                )

                AccessField(
                    label = "Last name",
                    value = lastName,
                    onValueChange = { lastName = it }
                )

                AccessField(
                    label = "Email address",
                    value = email,
                    onValueChange = { email = it },
                    keyboardType = KeyboardType.Email
                )

                AccessField(
                    label = "Phone number (e.g. 0712345678)",
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    keyboardType = KeyboardType.Phone
                )

                PasswordField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it }
                )

                PasswordField(
                    label = "Confirm password",
                    value = passwordConfirm,
                    onValueChange = { passwordConfirm = it }
                )

                Text(
                    text = "You must verify your email before accessing your account.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                AccessButton(
                    text = "Create Account",
                    busy = busy
                ) {
                    runAction {
                        repository.register(
                            email,
                            phoneNumber,
                            password,
                            passwordConfirm,
                            firstName,
                            lastName
                        )

                        password = ""
                        passwordConfirm = ""

                        navigate(AccessPage.VERIFY_EMAIL)

                        message = "Account created. Check your email for the verification code."
                    }
                }

                TextButton(
                    onClick = { navigate(AccessPage.LOGIN) }
                ) {
                    Text("Already have an account? Log in")
                }

                BackToWelcome {
                    navigate(AccessPage.WELCOME)
                }
            }

            AccessPage.VERIFY_EMAIL -> {

                AccessHeading(
                    title = "Verify Your Email",
                    subtitle = "Enter the verification code sent to your email address."
                )

                AccessField(
                    label = "Email address",
                    value = email,
                    onValueChange = { email = it },
                    keyboardType = KeyboardType.Email
                )

                AccessField(
                    label = "Verification code",
                    value = code,
                    onValueChange = { code = it },
                    keyboardType = KeyboardType.Number
                )

                AccessButton(
                    text = "Verify Email",
                    busy = busy
                ) {
                    runAction {
                        repository.verifyEmail(code)

                        code = ""
                        password = ""

                        navigate(AccessPage.LOGIN)

                        message = "Email verified successfully. You can now log in."
                    }
                }

                OutlinedButton(
                    enabled = !busy,
                    onClick = {
                        runAction {
                            repository.resendVerification(email)
                            message = "A new verification code has been requested."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Resend Verification Code")
                }

                TextButton(
                    onClick = { navigate(AccessPage.LOGIN) }
                ) {
                    Text("Back to Login")
                }
            }

            AccessPage.RESET_REQUEST -> {

                AccessHeading(
                    title = "Forgot Password?",
                    subtitle = "Enter your registered email address to receive password reset instructions."
                )

                AccessField(
                    label = "Email address",
                    value = email,
                    onValueChange = { email = it },
                    keyboardType = KeyboardType.Email
                )

                AccessButton(
                    text = "Send Reset Instructions",
                    busy = busy
                ) {
                    runAction {
                        repository.requestPasswordReset(email)

                        navigate(AccessPage.RESET_CONFIRM)

                        message = "If your account exists, password reset instructions have been sent."
                    }
                }

                TextButton(
                    onClick = { navigate(AccessPage.LOGIN) }
                ) {
                    Text("Back to Login")
                }
            }

            AccessPage.RESET_CONFIRM -> {

                AccessHeading(
                    title = "Reset Password",
                    subtitle = "Enter the reset code and choose a new password."
                )

                AccessField(
                    label = "Reset code",
                    value = code,
                    onValueChange = { code = it }
                )

                PasswordField(
                    label = "New password",
                    value = password,
                    onValueChange = { password = it }
                )

                PasswordField(
                    label = "Confirm new password",
                    value = passwordConfirm,
                    onValueChange = { passwordConfirm = it }
                )

                AccessButton(
                    text = "Reset Password",
                    busy = busy
                ) {
                    runAction {
                        repository.confirmPasswordReset(
                            code,
                            password,
                            passwordConfirm
                        )

                        code = ""
                        password = ""
                        passwordConfirm = ""

                        navigate(AccessPage.LOGIN)

                        message = "Password reset successfully. Log in with your new password."
                    }
                }

                TextButton(
                    onClick = { navigate(AccessPage.LOGIN) }
                ) {
                    Text("Back to Login")
                }
            }
        }

        if (busy) {
            Spacer(Modifier.height(12.dp))

            CircularProgressIndicator(
                color = Color(0xFF166534)
            )
        }

        error?.let {
            Spacer(Modifier.height(12.dp))

            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp
            )
        }

        message?.let {
            Spacer(Modifier.height(12.dp))

            Text(
                text = it,
                color = Color(0xFF166534),
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "© 2026 SCI BASE COLLECTIONS",
            fontSize = 11.sp,
            color = Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun AccessHeading(
    title: String,
    subtitle: String
) {
    Text(
        text = title,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF111827)
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = subtitle,
        fontSize = 14.sp,
        color = Color(0xFF64748B),
        lineHeight = 21.sp
    )

    Spacer(Modifier.height(22.dp))
}

@Composable
private fun AccessField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(14.dp),
        visualTransformation = if (visible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(
                onClick = { visible = !visible }
            ) {
                Icon(
                    imageVector = if (visible) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = if (visible) {
                        "Hide password"
                    } else {
                        "Show password"
                    }
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        )
    )
}

@Composable
private fun AccessButton(
    text: String,
    busy: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !busy,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF166534)
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun BackToWelcome(
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text("Back to Welcome")
    }
}