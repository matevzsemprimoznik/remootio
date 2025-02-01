import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun DefaultColumnItem(itemHeightDp: Dp, itemWidthDp: Dp, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .height(itemHeightDp)
            .width(itemWidthDp)
    ) {
        content()
    }
}

@Composable
fun LazyColumnWithSnap(
    itemHeightDp: Dp = 160.dp,
    itemWidthDp: Dp = 160.dp,
    items: List<String>,
    initialItem: Int = 0,
    viewportHeightDp: Dp,
    onChange: (String) -> Unit = {},
    columnItem: @Composable (String) -> Unit
) {
    val itemWithPaddingHeightPx = with(LocalDensity.current) { (itemHeightDp).toPx() }
    val itemHeightPx = with(LocalDensity.current) { itemHeightDp.toPx() }
    val viewportHeightPx = with(LocalDensity.current) { viewportHeightDp.toPx() }

    var currentIndex by remember { mutableIntStateOf(initialItem) }

    val listState = rememberLazyListState(
        initialFirstVisibleItemScrollOffset = initialItem * itemWithPaddingHeightPx.toInt()
    )

    val coroutineScope = rememberCoroutineScope()


    LazyColumn(
        state = listState,
        modifier = Modifier.height(viewportHeightDp)
    ) {
        itemsIndexed(items) { index, item ->
            Box(Modifier
                .padding(top = if (index == 0) (viewportHeightDp - itemHeightDp) / 2 else 0.dp)
                .padding(bottom = if (index == items.size - 1) (viewportHeightDp - itemHeightDp) / 2 else 0.dp)
            ) {
                DefaultColumnItem(itemHeightDp = itemHeightDp, itemWidthDp = itemWidthDp) {
                    columnItem(item)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling) {
                    val targetIndex = calculateTargetIndex(
                        listState.firstVisibleItemIndex,
                        listState.firstVisibleItemScrollOffset,
                        itemHeightPx,
                        items.size
                    )

                    if (currentIndex != targetIndex) {
                        currentIndex = targetIndex
                        onChange(items[targetIndex])
                    }

                    coroutineScope.launch {
                        listState.animateScrollToItem(index = targetIndex, scrollOffset = -(viewportHeightPx.toInt() - itemHeightPx.toInt()) / 2)
                    }

                }
            }
    }
}

fun calculateTargetIndex(
    firstVisibleItemIndex: Int,
    firstVisibleItemScrollOffset: Int,
    itemHeightPx: Float,
    itemCount: Int
): Int {
    val totalScrollOffset = firstVisibleItemIndex * itemHeightPx + firstVisibleItemScrollOffset

    var targetIndex = (totalScrollOffset / itemHeightPx).toInt()

    val visibleItemFraction = totalScrollOffset % itemHeightPx

    if (visibleItemFraction > itemHeightPx / 2) {
        targetIndex++
    }

    if (targetIndex >= itemCount - 1) {
        targetIndex = itemCount - 1
    }

    return targetIndex
}