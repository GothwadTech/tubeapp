package com.gothwad.tube.ui.components.donation

import androidx.annotation.StringRes

internal const val SUPPORT_URL = "https://www.gothwadtech.com"

internal class CryptoWallet(
    @StringRes val name: Int,
    val ticker: String,
    val address: String,
)

/**
 * Wallets listed on the donations page. PlusTube currently routes all support through the
 * Gothwad Tech website, so this stays empty; the donations screen hides the crypto section.
 */
internal val DonationWallets = emptyList<CryptoWallet>()
