package com.example.book_tracker.features.feature_user_list.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.user.MyUser

/**
 * UserListItem is used as UserList item at UserListScreen
 */
@Composable
fun UserListItem(
    modifier: Modifier = Modifier,
    user: MyUser,
//    visibleProgressBar: Boolean = true,
    onUserItemClick: () -> Unit

) {

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onUserItemClick()
            },
//        border = BorderStroke(0.1.dp, MaterialTheme.colors.primary)
    ) {

        Box {
            Row(
                modifier.padding(8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {

                if (user.photo != null) {
                    AsyncImage(
                        modifier = Modifier.fillMaxHeight().clip(CircleShape),
                        placeholder = painterResource(R.drawable.user_placeholder),
                        model = user.photo,
                        contentDescription = "user profile photo",
                    )
                } else {
                    Image(
                        modifier = Modifier.height(150.dp).clip(CircleShape),
                        painter = painterResource(R.drawable.user_placeholder),
                        contentDescription = "user profile photo",
                        contentScale = ContentScale.FillHeight,
                    )
                }

                Column(
                    modifier.padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                ) {

                    user.name?.let {
                        Text(
//                            modifier = modifier.padding(8.dp),
                            text = it,
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
                        )
                    }
//                    TextSection(book = book)
                }
            }
        }
    }
}