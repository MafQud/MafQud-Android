package com.mafqud.android.ui.material

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    numColumns: Int = 2,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->

        // Width per column based on constraints
        val columnWidth = (constraints.maxWidth / numColumns)

        // Make sure items can't be wider than column width
        val itemConstraints = constraints.copy(maxWidth = columnWidth)

        // Track the height of each column in an array.
        val columnHeights = IntArray(numColumns) { 0 }

        // For each item to place, figure out the shortest column so we know where to place it
        // and keep track of how large that column is going to be.
        val placeables = measurables.map { measurable ->
            val column = shortestColumn(columnHeights)
            val placeable = measurable.measure(itemConstraints)
            columnHeights[column] += placeable.height
            placeable
        }

        // Get the height of the entire grid, by using the largest column,
        // ensuring that it does not go out of the bounds
        // of this container.
        // If something went wrong, default to min height.
        val height = columnHeights.maxOrNull()
            ?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight

        layout(
            width = constraints.maxWidth,
            height = height,
        ) {
            // Keep track of the current Y position for each column
            val columnYPointers = IntArray(numColumns) { 0 }

            placeables.forEach { placeable ->
                // Determine which column to place this item in
                val column = shortestColumn(columnYPointers)

                placeable.place(
                    x = columnWidth * column,
                    y = columnYPointers[column],
                )

                // Update the pointer for this column based on the item
                // we just placed.
                columnYPointers[column] += placeable.height
            }
        }
    }
}

/**
 * Loop through all of the column heights, and determine which one is the shortest. This is how
 * we determine which column to add the next item to.
 */
private fun shortestColumn(columnHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var columnIndex = 0

    columnHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            columnIndex = index
        }
    }

    return columnIndex
}
