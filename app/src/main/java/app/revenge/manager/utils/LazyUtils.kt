package app.revenge.manager.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType

inline fun <T : Any> LazyListScope.itemsIndexed(
    items: LazyPagingItems<T>,
    noinline key: ((i: Int, item: T) -> Any)? = null,
    crossinline itemContent: @Composable (i: Int, item: T) -> Unit
) {
    items(
        count = items.itemCount,
        key = { i -> 
            val item = items[i]
            if (item != null) key?.invoke(i, item) ?: i else i
        },
        contentType = items.itemContentType()
    ) { i ->
        val item = items[i]
        if (item != null) {
            itemContent(i, item)
        }
    }
}