package app.revenge.manager.domain.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.revenge.manager.domain.manager.InstallManager
import app.revenge.manager.domain.manager.PreferenceManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InstallReceiver : BroadcastReceiver(), KoinComponent {

    private val installManager: InstallManager by inject()
    private val prefs: PreferenceManager by inject()

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val packageName = intent?.data?.schemeSpecificPart

        if (action == Intent.ACTION_PACKAGE_REMOVED && !intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
            packageName?.let {
                if (prefs.installedInstances.contains(it)) {
                    prefs.installedInstances = prefs.installedInstances - it
                }
            }
        }

        installManager.getInstalled()
    }
}