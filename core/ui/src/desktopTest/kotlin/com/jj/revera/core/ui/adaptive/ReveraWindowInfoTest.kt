package com.jj.revera.core.ui.adaptive

import androidx.compose.material3.adaptive.HingeInfo
import androidx.compose.material3.adaptive.Posture
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.ui.geometry.Rect
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.computeWindowSizeClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ReveraWindowInfoTest {
    private fun info(
        widthDp: Int,
        heightDp: Int,
        posture: Posture = Posture(),
    ): ReveraWindowInfo {
        val sizeClass = WindowSizeClass.BREAKPOINTS_V2.computeWindowSizeClass(widthDp, heightDp)
        val adaptive = WindowAdaptiveInfo(sizeClass, posture)
        return adaptive.toReveraWindowInfo(calculatePaneScaffoldDirective(adaptive))
    }

    @Test
    fun widthBreakpointsMapToClasses() {
        assertEquals(WindowWidthClass.Compact, info(411, 891).widthClass)
        assertEquals(WindowWidthClass.Medium, info(673, 841).widthClass)
        assertEquals(WindowWidthClass.Expanded, info(1000, 800).widthClass)
        assertEquals(WindowWidthClass.Large, info(1280, 800).widthClass)
        assertEquals(WindowWidthClass.ExtraLarge, info(1920, 1080).widthClass)
    }

    @Test
    fun panesGrowWithWidth() {
        assertEquals(1, info(411, 891).maxHorizontalPanes)
        assertEquals(2, info(1000, 800).maxHorizontalPanes)
    }

    @Test
    fun halfOpenedHorizontalHingeIsTabletop() {
        val hinge =
            HingeInfo(
                Rect(0f, 1000f, 1800f, 1040f),
                isFlat = false,
                isVertical = false,
                isSeparating = true,
                isOccluding = false,
            )
        val posture = info(673, 841, Posture(isTabletop = true, hingeList = listOf(hinge))).posture
        assertIs<FoldPosture.Tabletop>(posture)
        assertEquals(1000f, posture.hingeBounds.top)
    }

    @Test
    fun halfOpenedVerticalHingeIsBook() {
        val hinge =
            HingeInfo(
                Rect(900f, 0f, 940f, 2000f),
                isFlat = false,
                isVertical = true,
                isSeparating = true,
                isOccluding = false,
            )
        assertIs<FoldPosture.Book>(info(673, 841, Posture(hingeList = listOf(hinge))).posture)
    }

    @Test
    fun flatHingeIsFlat() {
        val hinge =
            HingeInfo(
                Rect(900f, 0f, 940f, 2000f),
                isFlat = true,
                isVertical = true,
                isSeparating = false,
                isOccluding = false,
            )
        assertEquals(FoldPosture.Flat, info(673, 841, Posture(hingeList = listOf(hinge))).posture)
    }
}
