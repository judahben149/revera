package com.jj.revera.core.designsystem.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jj.revera.core.designsystem.icons.ReveraIcons

@Composable
fun ReveraBackButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = ReveraIcons.ArrowBack, contentDescription = contentDescription)
    }
}
