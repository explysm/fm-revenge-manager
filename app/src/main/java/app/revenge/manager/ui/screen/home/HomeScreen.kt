package app.revenge.manager.ui.screen.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import app.revenge.manager.BuildConfig
import app.revenge.manager.R
import app.revenge.manager.domain.manager.PreferenceManager
import app.revenge.manager.ui.components.SegmentedButton
import app.revenge.manager.ui.screen.installer.InstallerScreen
import app.revenge.manager.ui.screen.settings.SettingsScreen
import app.revenge.manager.ui.theme.FireOrange
import app.revenge.manager.ui.theme.FireRed
import app.revenge.manager.ui.theme.FireYellow
import app.revenge.manager.ui.viewmodel.home.HomeViewModel
import app.revenge.manager.ui.widgets.AppIcon
import app.revenge.manager.ui.widgets.dialog.BatteryOptimizationDialog
import app.revenge.manager.ui.widgets.dialog.InstallOptionsDialog
import app.revenge.manager.ui.widgets.dialog.StoragePermissionsDialog
import app.revenge.manager.ui.widgets.home.CommitList
import app.revenge.manager.ui.widgets.updater.UpdateDialog
import app.revenge.manager.utils.Constants
import app.revenge.manager.utils.DiscordVersion
import app.revenge.manager.utils.glow
import app.revenge.manager.utils.navigate
import com.kyant.backdrop.liquid
import com.kyant.backdrop.liquifiable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.androidx.compose.get

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val prefs: PreferenceManager = get()
        val viewModel: HomeViewModel = getScreenModel()

        val currentVersion = remember(viewModel.installManager.current) {
            DiscordVersion.fromVersionCode(viewModel.installManager.current?.versionCode.toString())
        }

        val latestVersion =
            remember(prefs.packageName, prefs.discordVersion, viewModel.discordVersions, prefs.channel) {
                when {
                    prefs.discordVersion.isBlank() -> viewModel.discordVersions?.get(prefs.channel)
                    else -> DiscordVersion.fromVersionCode(prefs.discordVersion)
                }
            }

        var changelogExpanded by remember { mutableStateOf(false) }

        // == Dialogs == //

        StoragePermissionsDialog()
        BatteryOptimizationDialog()

        var showInstallOptions by remember { mutableStateOf(false) }

        if (showInstallOptions) {
            InstallOptionsDialog(
                versions = viewModel.discordVersions ?: emptyMap(),
                defaultVersion = latestVersion ?: Constants.DUMMY_VERSION,
                onDismiss = { showInstallOptions = false },
                onConfirm = { pkg, name, ver ->
                    showInstallOptions = false
                    prefs.packageName = pkg
                    prefs.appName = name
                    val verCode = ver.toVersionCode()
                    prefs.discordVersion = verCode
                    prefs.setTargetVersion(pkg, verCode)
                    viewModel.installManager.getInstalled()
                    navigator.navigate(InstallerScreen(ver))
                }
            )
        }

        if (
            viewModel.showUpdateDialog &&
            viewModel.release != null &&
            !BuildConfig.DEBUG
        ) {
            var progress by remember { mutableStateOf<Float?>(null) }

            UpdateDialog(
                release = viewModel.release!!,
                isUpdating = viewModel.isUpdating,
                progress = progress,
                onDismiss = { viewModel.showUpdateDialog = false },
                onConfirm = {
                    viewModel.downloadAndInstallUpdate { newProgress -> progress = newProgress }
                }
            )
        }

        // == Screen == //

        Scaffold(
            topBar = { TitleBar() },
            containerColor = MaterialTheme.colorScheme.background
        ) { pv ->
            Box(modifier = Modifier.fillMaxSize().liquifiable()) {
                // Fiery Background Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    FireRed.copy(alpha = 0.2f),
                                    FireOrange.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        top = pv.calculateTopPadding() + 16.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        AppIcon(
                            customIcon = prefs.patchIcon,
                            releaseChannel = prefs.channel,
                            modifier = Modifier
                                .size(100.dp)
                                .glow(FireOrange, radius = 30.dp, alpha = 0.6f)
                        )
                    }

                    item {
                        Text(
                            text = prefs.appName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                    }

                    if (prefs.installedInstances.isNotEmpty()) {
                        item {
                            Text(
                                text = "Managed Instances",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }
                        item {
                            val context = LocalContext.current
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(prefs.installedInstances.toList()) { pkg ->
                                    val isSelected = prefs.packageName == pkg
                                    val label = remember(pkg) {
                                        try {
                                            context.packageManager.getApplicationLabel(
                                                context.packageManager.getApplicationInfo(pkg, 0)
                                            ).toString()
                                        } catch (e: Exception) {
                                            pkg.split(".").last()
                                        }
                                    }

                                    ElevatedCard(
                                        onClick = {
                                            prefs.packageName = pkg
                                            prefs.appName = label
                                            prefs.discordVersion = prefs.getTargetVersion(pkg)
                                            viewModel.installManager.getInstalled()
                                        },
                                        shape = RoundedCornerShape(24.dp),
                                        colors = if (isSelected) CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        ) else CardDefaults.elevatedCardColors(),
                                        elevation = CardDefaults.elevatedCardElevation(
                                            defaultElevation = if (isSelected) 8.dp else 2.dp
                                        )
                                    ) {
                                        Text(
                                            text = label,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AnimatedVisibility(visible = currentVersion != null) {
                                Text(
                                    text = stringResource(
                                        R.string.version_current,
                                        currentVersion.toString()
                                    ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = LocalContentColor.current.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }

                            val latestLabel =
                                if (prefs.discordVersion.isNotBlank()) R.string.version_target else R.string.version_latest

                            AnimatedVisibility(visible = latestVersion != null) {
                                Text(
                                    text = stringResource(latestLabel, latestVersion.toString()),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = FireOrange,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                showInstallOptions = true
                            },
                            enabled = latestVersion != null && (prefs.allowDowngrade || latestVersion >= (currentVersion ?: Constants.DUMMY_VERSION)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .glow(FireRed.copy(alpha = 0.5f), radius = 10.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            val label = when {
                                latestVersion == null -> R.string.msg_loading
                                currentVersion == null -> R.string.action_install
                                currentVersion == latestVersion -> R.string.action_reinstall
                                latestVersion > currentVersion -> R.string.action_update
                                else -> if (prefs.allowDowngrade) R.string.msg_downgrade else R.string.msg_downgrade_disallowed
                            }

                            Text(
                                text = stringResource(label).uppercase(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                modifier = Modifier.basicMarquee()
                            )
                        }
                    }

                    item {
                        AnimatedVisibility(visible = viewModel.installManager.current != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(4.dp)
                            ) {
                                SegmentedButton(
                                    icon = Icons.Filled.OpenInNew,
                                    text = stringResource(R.string.action_launch),
                                    onClick = { viewModel.launchMod() }
                                )
                                SegmentedButton(
                                    icon = Icons.Filled.Info,
                                    text = stringResource(R.string.action_info),
                                    onClick = { viewModel.launchModInfo() }
                                )
                                SegmentedButton(
                                    icon = Icons.Filled.Delete,
                                    text = stringResource(R.string.action_uninstall),
                                    onClick = { viewModel.uninstallMod() }
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { changelogExpanded = !changelogExpanded }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Changelog",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (changelogExpanded) "Collapse" else "View All",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        val changelogHeight by animateDpAsState(
                            targetValue = if (changelogExpanded) 450.dp else 120.dp,
                            label = "ChangelogExpansion"
                        )
                        
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(changelogHeight),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            )
                        ) {
                            CommitList(
                                commits = viewModel.commits.collectAsLazyPagingItems()
                            )
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun TitleBar() {
        TopAppBar(
            title = { 
                Text(
                    stringResource(R.string.title_home),
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1).sp
                ) 
            },
            actions = { Actions() },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.liquid()
        )
    }

    @Composable
    private fun Actions() {
        val viewModel: HomeViewModel = getScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        IconButton(onClick = { viewModel.getDiscordVersions() }) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.action_reload),
                tint = FireOrange
            )
        }
        IconButton(onClick = { navigator.navigate(SettingsScreen()) }) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = stringResource(R.string.action_open_about)
            )
        }
    }

}