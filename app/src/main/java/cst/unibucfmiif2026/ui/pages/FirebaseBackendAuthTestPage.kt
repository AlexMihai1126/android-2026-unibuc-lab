package cst.unibucfmiif2026.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import cst.unibucfmiif2026.network.api.UsersApiService
import kotlinx.coroutines.launch

@Composable
fun FirebaseBackendAuthTestPage(
    usersApiService: UsersApiService,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val firebaseUser = FirebaseAuth.getInstance().currentUser

    var isLoading by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("Not tested yet.") }
    var detailsText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Firebase Backend Auth Test",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Current Firebase User",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "UID: ${firebaseUser?.uid ?: "Not logged in"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Email: ${firebaseUser?.email ?: "No email"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    statusText = "Testing backend token verification..."
                    detailsText = null

                    try {
                        val response = usersApiService.getPermissions()

                        if (response.isSuccessful) {
                            val body = response.body()

                            statusText = "Success: backend accepted the Firebase token."

                            detailsText = buildString {
                                appendLine("HTTP ${response.code()}")
                                appendLine("Message: ${body?.message ?: "No message"}")
                                appendLine("Backend user UID: ${body?.user?.uid ?: "No UID returned"}")
                                appendLine("Backend user email: ${body?.user?.email ?: "No email returned"}")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()

                            statusText = "Failed: backend rejected the request."

                            detailsText = buildString {
                                appendLine("HTTP ${response.code()}")
                                appendLine(errorBody ?: "No error body returned.")
                            }
                        }
                    } catch (e: Exception) {
                        statusText = "Error: request failed before reaching a valid response."

                        detailsText = e.message ?: e::class.java.simpleName
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading && firebaseUser != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Test Firebase Token")
            }
        }

        if (firebaseUser == null) {
            Text(
                text = "You are not logged in. Log in first, then test the backend route.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium
                )

                detailsText?.let {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        TextButton(
            onClick = {
                statusText = "Not tested yet."
                detailsText = null
            }
        ) {
            Text("Reset")
        }
    }
}