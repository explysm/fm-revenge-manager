package app.revenge.manager.network.service

import app.revenge.manager.domain.manager.PreferenceManager
import app.revenge.manager.network.dto.Commit
import app.revenge.manager.network.dto.Index
import app.revenge.manager.network.dto.Release
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestService(
    private val httpService: HttpService,
    private val prefs: PreferenceManager
) {

    private val githubBaseUrl: String
        get() = if (prefs.bypassIspBlocks) prefs.proxyUrl else prefs.githubApiUrl

    suspend fun getLatestRelease(repo: String) = withContext(Dispatchers.IO) {
        httpService.request<Release> {
            url("$githubBaseUrl/repos/$repo/releases/latest")
        }
    }

    suspend fun getLatestDiscordVersions() = withContext(Dispatchers.IO) {
        httpService.request<Index> {
            url("${prefs.mirror.baseUrl}/tracker/index")
        }
    }

    suspend fun getCommits(repo: String, page: Int = 1) = withContext(Dispatchers.IO) {
        httpService.request<List<Commit>> {
            url("$githubBaseUrl/repos/$repo/commits")
            parameter("page", page)
        }
    }

}