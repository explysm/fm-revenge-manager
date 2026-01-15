package app.revenge.manager.ui.widgets.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    
    // Selection state: Stable, Beta, Alpha, Custom
    var selectionMode by remember { mutableStateOf("Stable") }
    var customVersionInput by remember { mutableStateOf("") }
    
    val selectedVersion = remember(selectionMode, customVersionInput, versions) {
        when (selectionMode) {
            "Stable" -> versions[DiscordVersion.Type.STABLE] ?: defaultVersion
            "Beta" -> versions[DiscordVersion.Type.BETA] ?: defaultVersion
            "Alpha" -> versions[DiscordVersion.Type.ALPHA] ?: defaultVersion
            else -> DiscordVersion.fromVersionCode(customVersionInput)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.action_install)) },
        icon = { Icon(Icons.Outlined.Download, null) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
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

                listOf("Stable", "Beta", "Alpha", "Custom").forEach { mode ->
                    val version = when(mode) {
                        "Stable" -> versions[DiscordVersion.Type.STABLE]
                        "Beta" -> versions[DiscordVersion.Type.BETA]
                        "Alpha" -> versions[DiscordVersion.Type.ALPHA]
                        else -> null
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectionMode = mode }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectionMode == mode,
                            onClick = { selectionMode = mode }
                        )
                        Text(
                            text = if (version != null) "$mode (${version})" else mode,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    
                    if (mode == "Custom") {
                        AnimatedVisibility(visible = selectionMode == "Custom") {
                            Column {
                                OutlinedTextField(
                                    value = customVersionInput,
                                    onValueChange = { customVersionInput = it },
                                    label = { Text("Version Code") },
                                    placeholder = { Text("e.g. 211021") },
                                    singleLine = true,
                                    isError = selectionMode == "Custom" && selectedVersion == null && customVersionInput.isNotBlank(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 32.dp, top = 4.dp)
                                )
                                if (selectionMode == "Custom" && selectedVersion == null && customVersionInput.isNotBlank()) {
                                    Text(
                                        text = stringResource(R.string.msg_invalid_version),
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(start = 32.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = { selectedVersion?.let { onConfirm(packageName, appName, it) } },
                enabled = packageName.isNotBlank() && appName.isNotBlank() && selectedVersion != null
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
