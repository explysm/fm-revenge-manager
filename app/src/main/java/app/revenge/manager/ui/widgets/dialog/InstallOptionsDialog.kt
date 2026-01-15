package app.revenge.manager.ui.widgets.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.revenge.manager.BuildConfig
import app.revenge.manager.R
import app.revenge.manager.utils.DiscordVersion

@Composable
fun InstallOptionsDialog(
    versions: Map<DiscordVersion.Type, DiscordVersion?>,
    defaultVersion: DiscordVersion,
    onDismiss: () -> Unit,
    onConfirm: (packageName: String, appName: String, version: DiscordVersion) -> Unit
) {
    var packageName by remember { mutableStateOf(BuildConfig.MODDED_APP_PACKAGE_NAME) }
    var appName by remember { mutableStateOf(BuildConfig.MOD_NAME) }
    var selectedVersion by remember { mutableStateOf(defaultVersion) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.action_install)) },
        icon = { Icon(Icons.Outlined.Download, null) },
        text = {
            Column {
                OutlinedTextField(
                    value = appName,
                    onValueChange = { appName = it },
                    label = { Text("App Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = packageName,
                    onValueChange = { packageName = it },
                    label = { Text("Package Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (packageName == BuildConfig.MODDED_APP_PACKAGE_NAME) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Warning: This will override the original FireCord installation.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Version",
                    style = MaterialTheme.typography.labelLarge
                )

                versions.forEach { (type, version) ->
                    if (version != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedVersion = version }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedVersion == version,
                                onClick = { selectedVersion = version }
                            )
                            Text(
                                text = version.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = { onConfirm(packageName, appName, selectedVersion) },
                enabled = packageName.isNotBlank() && appName.isNotBlank()
            ) {
                Text(stringResource(R.string.action_install))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_dismiss_nevermind))
            }
        }
    )
}
