package com.example.book_tracker.features.feature_search.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.model.book.VolumeInfo

/**
 * BookItemSearch is used as BookList item at SearchScreen
 */
@Composable
fun BookItemSearch(
    modifier: Modifier = Modifier,
    item: Item,
    onBookItemClick: () -> Unit,
    onAddClick: (Item, Boolean) -> Unit,

    ) {
    val bookVolumeInfo = item.volumeInfo
    var isAdded by rememberSaveable { mutableStateOf(false) }

    println(bookVolumeInfo.toString())

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(4.dp)
            .height(150.dp)
            .clip(MaterialTheme.shapes.small)
            .fillMaxWidth()
            .clickable { onBookItemClick() },
    ) {

        Row(
            modifier = modifier
//                .padding(0.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ThumbnailSection(bookVolumeInfo)
            TextSection(modifier = modifier.weight(1f), book = bookVolumeInfo)
            IconButton(
                modifier = modifier.padding(),
                enabled = !isAdded,
                onClick = {
                    isAdded = !isAdded
                    onAddClick(item, isAdded)
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}

@Composable
private fun ThumbnailSection(book: VolumeInfo?) {
    if (book?.imageLinks?.smallThumbnail != null) {
        AsyncImage(
            placeholder = painterResource(R.drawable.book_cover_placeholder),
            model = book.imageLinks.smallThumbnail,
            modifier = Modifier.fillMaxHeight(),
            contentDescription = "Book Cover Image",
        )
    } else {
        Image(
            painter = painterResource(R.drawable.book_cover_placeholder),
            contentDescription = "Book Cover Image",
            modifier = Modifier.height(150.dp),
            contentScale = ContentScale.FillHeight,
        )
    }
}

@Composable
private fun TextSection(
    modifier: Modifier = Modifier,
    book: VolumeInfo?,
) {

    val labelSmall = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Normal)

    Column(
        modifier = modifier.padding(start = 8.dp, top = 6.dp, end = 6.dp, bottom = 6.dp),
//            .fillMaxWidth()
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {

        book?.title?.let {
            MyText(
                text = it,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
                maxLines = 2
            )
        }

        book?.subtitle?.let {
            MyText(text = it, style = MaterialTheme.typography.body2, maxLines = 1)
        }

        Spacer(modifier = Modifier.height(16.dp))

        book?.authors?.let {
            MyText(text = it.joinToString(), style = MaterialTheme.typography.body2)
        }
        book?.publishedDate?.let {
            MyText(text = " • " + it.slice(0..3))
        }

        book?.publisher?.let {
            MyText(text = " • $it")
        }
        book?.averageRating?.let {
            MyText(text = " • $it ⭐︎︎")
        }

        book?.categories?.let {
            MyText(text = " • " + it.joinToString())
        }

//        book?.mainCategory?.let {
//            MyText(text = " • " + it)
//        }

//        book?.description?.let {
//            MyText(text = " • " + HtmlToStringConverter(it), maxLines = 3)
//        }
    }
}


@Composable
private fun MyText(
    text: String,
    style: TextStyle = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Normal),
    maxLines: Int = 1
) {
    Text(
        text = text,
        style = style,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}
