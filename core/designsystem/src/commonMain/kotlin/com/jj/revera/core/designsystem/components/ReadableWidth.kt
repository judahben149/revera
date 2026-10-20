package com.jj.revera.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Modifier
import com.jj.revera.core.designsystem.theme.ReveraDimens

/** Fills the width up to [ReveraDimens.ReadableContentMaxWidth], centered beyond that. */
fun Modifier.readableWidth(): Modifier =
    this
        .fillMaxWidth()
        .wrapContentWidth()
        .widthIn(max = ReveraDimens.ReadableContentMaxWidth)
        .fillMaxWidth()
