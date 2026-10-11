package com.gothwad.tube.ui.screens.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gothwad.tube.R
import com.gothwad.tube.ui.components.shared.FlowSheetHeader
import com.gothwad.tube.ui.components.shared.rememberFlowSheetState

private const val FLOW_REPO_URL = "https://github.com/A-EDev/Flow"

/**
 * Everyone who built the original Flow project, which PlusTube is based on. Keeping this list in
 * the app honours the GPL-3.0 attribution requirement and the project's own credit notice.
 */
private val FlowContributors =
    listOf(
        "A-EDev", "my-live-is-a-lie", "Mickael81", "Kefir2105",
        "aaasajsldfjsladkfj", "MattSolo451", "siggi1984", "Matthew3955",
        "restlessSangoLeelo", "Kuroo-tech", "Chiehx0220", "arifpedia",
        "whistlingwoods", "AhmedRX20", "saran2020", "Sup-25498",
        "ninosandzak", "NeoTurcios", "FabiRich", "SilentCoderHere",
        "Dyxux", "shiaho777", "JonatasVi", "Hallo5000",
        "mrqubic", "ajuckler", "CunaOcalCat", "PONYATIN",
        "mvanhorn", "Jorge-tin", "DiegoLpVn", "hmminterestinginteresting892",
        "TFQ0", "Lpzzo", "whAreyodIG", "wynriu",
        "SantosSi", "SaymoNBruh", "nimesh95-afk", "neocryptics",
        "mrmazay", "cozalnxkpahfo", "Vim-01", "OshekharO",
        "Roseclear", "Rodinamomen", "Riveting2180", "ItsMe-95fx",
        "Gh0zd", "fco-pires", "erroroftheworld", "AmyJeanes",
        "myrfamily93110515-ux", "PeterXMR", "xqrbb", "PastaHimself",
        "mohamedIbrahim18", "mishalshanavas", "MiskaToMiska", "Jsemfantazie5",
        "mipezraro", "stardust248397", "stysus", "Uddhav0Khirdikar",
        "duckesteles", "Oriiiiiiiiii", "nugraha-abd", "flytothehighest",
        "zmni", "alreadyhaveanaccount-signin", "DaRandomCube", "AdnanSilajdzic",
        "owlultra", "ao8midori", "ArthyDeveloper", "BotHunting",
        "Dav0009", "Dessalines39394", "doctorreditshere", "gkeyes",
        "Gato-Chamuscador", "hamim-ifty", "himepro923", "ITSZXY",
        "juancrubin08", "MXC48", "MaicolJMO", "Slayer015"
    )

/** Credits the upstream Flow project and lists all of its contributors. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SpecialThanksSheet(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val open = { url: String -> runCatching { uriHandler.openUri(url) } }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberFlowSheetState(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        FlowSheetHeader(
            title = stringResource(R.string.section_special_thanks),
            subtitle = stringResource(R.string.special_thanks_flow_subtitle),
            onClose = onDismiss,
            showDragHandle = false,
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item(key = "thanks.source") {
                TextButton(onClick = { open(FLOW_REPO_URL) }) {
                    Text(stringResource(R.string.special_thanks_open_flow_repo))
                }
            }
            items(FlowContributors, key = { "thanks.contrib.$it" }) { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 2.dp),
                )
            }
            item(key = "thanks.end") {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Text(
                        text = stringResource(R.string.about_gothwad_family),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
