package com.example.book_tracker.features.feature_user_list.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.book_tracker.core.domain.model.user.MyUser

/**
 * UserList is used as LazyList at UserListScreen
 */
@Composable
fun UserList(
    users: List<MyUser>,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    item: @Composable (MyUser) -> Unit
) {
    LazyColumn(
        state = lazyColumnState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(users) {

            item(it)
        }
    }
}