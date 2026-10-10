package com.example.kitabu.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.kitabu.ui.theme.Gold
import com.example.kitabu.ui.theme.Indigo

private val RoseSpacing = 22.dp
private val RoseHeight = 12.dp

@Composable
fun RoseStrip(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(RoseHeight)
            .background(Indigo)
    ) {
        val spacing = RoseSpacing.toPx()
        val centerY = size.height / 2
        var x = spacing / 2
        while (x < size.width + spacing / 2) {
            drawCircle(Gold, radius = 4.6.dp.toPx(), center = Offset(x, centerY))
            drawCircle(Indigo, radius = 2.3.dp.toPx(), center = Offset(x, centerY))
            drawCircle(Gold, radius = 1.dp.toPx(), center = Offset(x, centerY))
            x += spacing
        }
    }
}

@Composable
fun TextileBand(
    modifier: Modifier = Modifier,
    padStatusBar: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val base = modifier.fillMaxWidth().background(Indigo)
    Column(modifier = if (padStatusBar) base.statusBarsPadding() else base) {
        RoseStrip()
        content()
        RoseStrip()
    }
}
