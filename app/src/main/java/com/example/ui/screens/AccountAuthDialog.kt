package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.network.AccountApiException
import com.example.network.AccountProfileDto
import com.example.network.AccountRepository
import kotlinx.coroutines.launch

private enum class AccountFlow { LOGIN, REGISTER, VERIFY_EMAIL, RESET_REQUEST, RESET_CONFIRM, PROFILE }

@Composable
fun AccountAuthDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { AccountRepository(context.applicationContext) }
    val scope = rememberCoroutineScope()
    var flow by remember { mutableStateOf(AccountFlow.LOGIN) }
    var profile by remember { mutableStateOf<AccountProfileDto?>(null) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        busy = true
        profile = repository.restoreSession()
        profile?.let {
            email = it.email
            firstName = it.firstName
            lastName = it.lastName
            flow = AccountFlow.PROFILE
        }
        busy = false
    }

    fun runAction(action: suspend () -> Unit) {
        if (busy) return
        scope.launch {
            busy = true
            error = null
            message = null
            try {
                action()
            } catch (exception: AccountApiException) {
                error = exception.message
            } catch (_: Exception) {
                error = "The account request could not be completed. Try again when connected."
            } finally {
                busy = false
            }
        }
    }

    val title = when (flow) {
        AccountFlow.LOGIN -> "Sign in"
        AccountFlow.REGISTER -> "Create account"
        AccountFlow.VERIFY_EMAIL -> "Verify email"
        AccountFlow.RESET_REQUEST -> "Password recovery"
        AccountFlow.RESET_CONFIRM -> "Reset password"
        AccountFlow.PROFILE -> "Account profile"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                when (flow) {
                    AccountFlow.LOGIN -> {
                        AccountField("Email", email, { email = it }, keyboardType = KeyboardType.Email)
                        AccountField("Password", password, { password = it }, password = true)
                    }
                    AccountFlow.REGISTER -> {
                        AccountField("First name", firstName, { firstName = it })
                        AccountField("Last name", lastName, { lastName = it })
                        AccountField("Email", email, { email = it }, keyboardType = KeyboardType.Email)
                        AccountField("Password (12 characters or more)", password, { password = it }, password = true)
                        AccountField("Confirm password", passwordConfirm, { passwordConfirm = it }, password = true)
                    }
                    AccountFlow.VERIFY_EMAIL -> {
                        Text("Enter the one-time code sent to $email.")
                        AccountField("Verification code", code, { code = it })
                    }
                    AccountFlow.RESET_REQUEST -> {
                        AccountField("Email", email, { email = it }, keyboardType = KeyboardType.Email)
                    }
                    AccountFlow.RESET_CONFIRM -> {
                        AccountField("Reset code", code, { code = it })
                        AccountField("New password", password, { password = it }, password = true)
                        AccountField("Confirm new password", passwordConfirm, { passwordConfirm = it }, password = true)
                    }
                    AccountFlow.PROFILE -> {
                        Text(email, style = MaterialTheme.typography.bodyMedium)
                        Text("Email verified", color = MaterialTheme.colorScheme.primary)
                        AccountField("First name", firstName, { firstName = it })
                        AccountField("Last name", lastName, { lastName = it })
                    }
                }

                message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (busy) CircularProgressIndicator(modifier = Modifier.padding(8.dp))

                when (flow) {
                    AccountFlow.LOGIN -> {
                        Button(
                            enabled = !busy,
                            onClick = {
                                runAction {
                                    profile = repository.login(email, password)
                                    profile?.let {
                                        firstName = it.firstName
                                        lastName = it.lastName
                                    }
                                    password = ""
                                    flow = AccountFlow.PROFILE
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Sign in") }
                        TextButton(onClick = { flow = AccountFlow.REGISTER; error = null; message = null }) {
                            Text("Create an account")
                        }
                        TextButton(onClick = { flow = AccountFlow.RESET_REQUEST; error = null; message = null }) {
                            Text("Forgot password?")
                        }
                        TextButton(onClick = { flow = AccountFlow.VERIFY_EMAIL; error = null; message = null }) {
                            Text("Enter a verification code")
                        }
                    }
                    AccountFlow.REGISTER -> {
                        Button(
                            enabled = !busy,
                            onClick = {
                                runAction {
                                    repository.register(email, password, passwordConfirm, firstName, lastName)
                                    password = ""
                                    passwordConfirm = ""
                                    flow = AccountFlow.VERIFY_EMAIL
                                    message = "Check your email for a verification code."
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Create account") }
                        TextButton(onClick = { flow = AccountFlow.LOGIN }) { Text("Back to sign in") }
                    }
                    AccountFlow.VERIFY_EMAIL -> {
                        Button(
                            enabled = !busy,
                            onClick = {
                                runAction {
                                    repository.verifyEmail(code)
                                    code = ""
                                    flow = AccountFlow.LOGIN
                                    message = "Email verified. Sign in to continue."
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Verify email") }
                        TextButton(
                            enabled = !busy,
                            onClick = { runAction { repository.resendVerification(email) } },
                        ) { Text("Send another code") }
                        TextButton(onClick = { flow = AccountFlow.LOGIN }) { Text("Back to sign in") }
                    }
                    AccountFlow.RESET_REQUEST -> {
                        Button(
                            enabled = !busy,
                            onClick = {
                                runAction {
                                    repository.requestPasswordReset(email)
                                    flow = AccountFlow.RESET_CONFIRM
                                    message = "If the account exists, reset instructions have been sent."
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Send reset instructions") }
                        TextButton(onClick = { flow = AccountFlow.LOGIN }) { Text("Back to sign in") }
                    }
                    AccountFlow.RESET_CONFIRM -> {
                        Button(
                            enabled = !busy,
                            onClick = {
                                runAction {
                                    repository.confirmPasswordReset(code, password, passwordConfirm)
                                    code = ""
                                    password = ""
                                    passwordConfirm = ""
                                    flow = AccountFlow.LOGIN
                                    message = "Password reset. Sign in with the new password."
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Reset password") }
                        TextButton(onClick = { flow = AccountFlow.LOGIN }) { Text("Back to sign in") }
                    }
                    AccountFlow.PROFILE -> {
                        Button(
                            enabled = !busy,
                            onClick = {
                                runAction {
                                    profile = repository.updateProfile(firstName, lastName)
                                    message = "Profile saved."
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Save profile") }
                        OutlinedButton(
                            enabled = !busy,
                            onClick = {
                                runAction {
                                    repository.logout()
                                    profile = null
                                    password = ""
                                    flow = AccountFlow.LOGIN
                                    message = "Signed out. Offline features are still available."
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("Sign out") }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
    )
}

@Composable
private fun AccountField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    )
}
