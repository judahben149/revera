package com.jj.revera.core.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.jj.revera.core.designsystem.theme.ReveraTheme

@Composable
fun ReveraSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier =
            modifier
                .semantics { heading() }
                .padding(
                    start = ReveraTheme.spacing.lg,
                    end = ReveraTheme.spacing.lg,
                    top = ReveraTheme.spacing.xl,
                    bottom = ReveraTheme.spacing.sm,
                ),
    )
}
