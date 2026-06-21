package com.momosi.trucktrack.core.uilibrary.icons.vectors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

internal val TrailerVectorIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "TrailerTruck",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        addPath(
            pathData = PathParser().parsePathString(
                "M16.209,15.97C16.209,14.792 17.223,13.837 18.474,13.837C19.725,13.837 20.739,14.792 " +
                    "20.739,15.97C20.739,17.149 19.725,18.104 18.474,18.104C17.223,18.104 16.209,17.149 " +
                    "16.209,15.97ZM19.444,15.97C19.444,15.466 19.01,15.056 18.474,15.056C17.938,15.056 " +
                    "17.503,15.466 17.503,15.97C17.503,16.475 17.938,16.885 18.474,16.885C19.01,16.885 " +
                    "19.444,16.476 19.444,15.97ZM11.033,15.97C11.033,14.792 12.047,13.837 13.297,13.837C" +
                    "14.548,13.837 15.562,14.792 15.562,15.97C15.562,17.149 14.548,18.104 13.297,18.104C" +
                    "12.047,18.104 11.033,17.149 11.033,15.97ZM14.268,15.97C14.268,15.466 13.833,15.056 " +
                    "13.297,15.056C12.761,15.056 12.327,15.466 12.327,15.97C12.327,16.475 12.761,16.885 " +
                    "13.297,16.885C13.833,16.885 14.268,16.476 14.268,15.97Z",
            ).toNodes().toList(),
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.EvenOdd,
        )
        addPath(
            pathData = PathParser().parsePathString(
                "M11.405,13.228H20.36L21.306,14.788C21.394,14.933 21.595,14.986 21.751,14.901L22.308,14.602C" +
                    "22.467,14.516 22.519,14.33 22.43,14.183L21.85,13.228H22.353C22.71,13.228 23,12.955 " +
                    "23,12.618V5.609C23,5.273 22.71,5 22.353,5H1.647C1.29,5 1,5.273 1,5.609V12.618C1,12.955 " +
                    "1.29,13.228 1.647,13.228H2.294V14.145C2.294,14.309 2.44,14.447 2.621,14.447H3.262C" +
                    "3.445,14.447 3.588,14.312 3.588,14.145V13.228H9.914L9.335,14.183C9.247,14.328 9.3,14.517 " +
                    "9.457,14.602L10.014,14.901C10.172,14.987 10.369,14.935 10.458,14.788L11.405,13.228Z",
            ).toNodes().toList(),
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.EvenOdd,
        )
    }.build()
}
