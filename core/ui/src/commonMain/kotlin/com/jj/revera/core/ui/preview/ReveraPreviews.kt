package com.jj.revera.core.ui.preview

import androidx.compose.ui.tooling.preview.Preview

/** Renders a preview at the window sizes Revera must look right on. */
@Preview(name = "Phone", widthDp = 411, heightDp = 891, showBackground = true)
@Preview(name = "Foldable (unfolded)", widthDp = 673, heightDp = 841, showBackground = true)
@Preview(name = "Tablet", widthDp = 1280, heightDp = 800, showBackground = true)
@Preview(name = "Desktop / Googlebook", widthDp = 1440, heightDp = 900, showBackground = true)
annotation class PreviewReveraDevices
