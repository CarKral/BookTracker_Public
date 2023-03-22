package com.example.book_tracker.features.feature_book.presentation.book_detail_search

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.model.book.VolumeInfo
import com.example.book_tracker.core.presentation.components.LoadingRowWithText
import com.example.book_tracker.core.presentation.components.SnackbarManager
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.util.htmlToStringConverter
import com.example.book_tracker.features.feature_book.presentation.book_detail_search.BookDetailSearchState.UiState
import kotlinx.coroutines.launch

@Composable
fun BookDetailSearchScreen(
    state: BookDetailSearchState,
    viewModel: BookDetailSearchScreenViewModel,
    popUp: () -> Unit,
    openScreen: (String) -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getBook()
    }

    Scaffold(
        topBar = {
            BookDetailSearchScreenTopAppBar(
                popUp = popUp,
                isAddedToLibrary = state.isAddedToLibrary,
                openDetail = { openScreen(BookTrackerScreens.BookDetailJsonScreen.route + "?bookId=${state.bookId}") },
                onAddBook = {
                    viewModel.addBookToLibrary(
                        onSuccess = {
                            SnackbarManager.showMessage(R.string.successfully_added_to_library)
                        },
                        onExist = { exist ->
                            if (exist) SnackbarManager.showMessage(R.string.book_already_exist_in_library)
                        }
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state.uiState) {
                is UiState.Loading -> LoadingRowWithText(text = stringResource(R.string.loading))
                is UiState.Success -> {
                    if (state.item != null) BookDetailSearchScreenContent(item = state.item)
                    else LoadingRowWithText(text = stringResource(R.string.nothing_to_see))
                }
                is UiState.Error -> Text(text = "Error: ${state.uiState.throwable}")
            }
        }
    }
}

@Composable
fun BookDetailSearchScreenTopAppBar(
    popUp: () -> Unit,
    isAddedToLibrary: Boolean,
//    onCurrentReadClick: () -> Unit,
    openDetail: () -> Unit,
    onAddBook: () -> Unit,
) {
    TopAppBar(
        title = {
            Text("Detail knihy - Google Books")
        },
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(
                    Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "back"
                )
            }
        },
        actions = {
            IconButton(onClick = openDetail) {
                Icon(
                    Icons.Filled.Info,
                    tint = Color.White,
                    contentDescription = "Json Info"
                )
            }
            if (!isAddedToLibrary) {
                IconButton(onClick = onAddBook) {
                    Icon(
                        Icons.Filled.Add,
                        tint = Color.White,
                        contentDescription = "Add to library"
                    )
                }
            }
        },
    )
}

@Composable
fun BookDetailSearchScreenContent(
    modifier: Modifier = Modifier,
    item: Item?
) {

    val book = item?.volumeInfo
    val googleBookId = item?.id

    println(book.toString())

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ThumbnailSection(book = book)

        book?.title?.let {
            Text(
                text = it,
                modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center,
            )
        }

        book?.subtitle?.let {
            Text(
                text = it,
                modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
        }

        book?.authors?.let {
            Text(
                text = it.joinToString(),
                modifier.padding(16.dp),
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
    }

    Divider(thickness = 0.5.dp, modifier = modifier.padding(8.dp))

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val labelText = MaterialTheme.typography.body2
        val contentText = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)

        book?.pageCount?.let {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Počet stran", style = labelText)
                Text(
                    modifier = modifier.padding(8.dp),
                    text = it.toString(), style = contentText
                )
            }
        }
        book?.publishedDate?.let {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Vydáno", style = labelText)
                Text(
                    modifier = modifier.padding(8.dp),
                    text = it.slice(0..3), style = contentText
                )
            }
        }
        book?.language?.let {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Jazyk", style = labelText)
                Text(
                    modifier = modifier.padding(8.dp),
                    text = it, style = contentText
                )
            }
        }
        book?.averageRating?.let {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Hodnocení", style = labelText)
                Text(
                    modifier = modifier.padding(8.dp),
                    text = it.toString(), style = contentText
                )
            }
        }
    }

    Divider(thickness = 0.5.dp)

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        var extendedDescription by remember { mutableStateOf(false) }
        val labelText = MaterialTheme.typography.h6
        val contentText = MaterialTheme.typography.body2
        val context = LocalContext.current

        book?.description?.let {
            Row(
                modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Popis:", style = labelText
                )
                Spacer(modifier = modifier.weight(1f))



                TextButton(onClick = { extendedDescription = !extendedDescription }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = modifier.padding(horizontal = 8.dp),
                            text = if (extendedDescription) "Ukaž méně" else "Ukaž víc",
                            style = MaterialTheme.typography.button
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "More description"
                        )
                    }
                }
            }
            Text(
                modifier = modifier.padding(horizontal = 8.dp),
                text = htmlToStringConverter(it),
                style = contentText,
                maxLines = if (!extendedDescription) 3 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis
            )
        }

        book?.mainCategory?.let {
            TextSection("Hlavní žánr:", it, labelText, contentText)
        }

        book?.categories?.let {
            TextSection("Žánr:", it.joinToString(), labelText, contentText)
        }

        book?.publisher?.let {
            TextSection("Vydal:", it, labelText, contentText)
        }

        book?.language?.let {
            TextSection("Jazyk:", it, labelText, contentText)
        }

        book?.industryIdentifiers?.let {
            val identifiers = it.map { identifier ->
                identifier.type + " - " + identifier.identifier
            }
            TextSection("Kódy:", identifiers.joinToString(separator = "\n"), labelText, contentText)
        }

        book?.dimensions?.let {
            TextSection(
                "Rozměry:",
                "Výška: ${it.height}, Šířka: ${it.width}, Tloušťka: ${it.thickness}",
                labelText,
                contentText
            )
        }

        book?.canonicalVolumeLink?.let {
            LinkSection("Link Google Play:", it, labelText, contentText, context)
        }

        book?.infoLink?.let {
            LinkSection("Info link Google Play:", it, labelText, contentText, context)
        }

        book?.previewLink?.let {
            LinkSection("Preview Link Google Books:", it, labelText, contentText, context)
        }

        Divider(thickness = 0.5.dp, modifier = modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun TextSection(header: String, text: String, labelText: TextStyle, contentText: TextStyle) {
    Divider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
    Text(text = header, style = labelText)
    Spacer(Modifier.height(8.dp))
    Text(modifier = Modifier.padding(horizontal = 8.dp), text = text, style = contentText)
}

@Composable
fun LinkSection(
    text: String,
    link: String,
    labelText: TextStyle,
    contentText: TextStyle,
    context: Context
) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

    Divider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = text, style = labelText)
        Spacer(Modifier.width(8.dp))
        TextButton(onClick = { context.startActivity(intent) }) {
            Text(text = "Odkaz ZDE")
        }
    }
}

//@OptIn(ExperimentalPagerApi::class)
@Composable
fun ThumbnailSection(
    modifier: Modifier = Modifier,
    book: VolumeInfo?
) {

    if (book?.imageLinks != null) {
        val images = listOf(
            book.imageLinks.thumbnail,
            book.imageLinks.smallThumbnail,
            book.imageLinks.small,
            book.imageLinks.medium,
            book.imageLinks.large,
            book.imageLinks.extraLarge,
        )
        val scope = rememberCoroutineScope()
        val lazyRowState = rememberLazyListState()

        var showBigImage by remember { mutableStateOf(false) }
        var currentPage by remember { mutableStateOf(0) }
        var currentImageLink by remember { mutableStateOf(images[currentPage]) }

        ThumbnailSectionItem(showBigImage = showBigImage, image = images[currentPage],
            onImageClick = {
                currentImageLink = images[currentPage].toString()
                showBigImage = !showBigImage
            })

        Spacer(modifier = modifier.height(16.dp))

        LazyRow(
            state = lazyRowState,
        ) {
            itemsIndexed(images) { index, image ->
                ThumbnailPreviewSectionItem(image = image, selected = currentPage == index,
                    onPreviewImageClick = {
                        currentPage = index
                        scope.launch {
//                        pagerState.animateScrollToPage(index)
                        }
                    })
            }
        }
    } else {
        Surface(
            modifier = modifier
                .padding(16.dp)
                .shadow(elevation = 16.dp, clip = true)
                .clip(MaterialTheme.shapes.small)
                .clickable { },
        ) {
            Image(
                painter = painterResource(R.drawable.book_cover_placeholder),
                contentDescription = "Book Cover Image",
                modifier = modifier.height(200.dp),
                contentScale = ContentScale.FillHeight,
            )

        }
    }
}

@Composable
fun ThumbnailSectionItem(
    modifier: Modifier = Modifier,
    showBigImage: Boolean,
    image: String?,
    onImageClick: () -> Unit
) {

    Surface(
        modifier
            .padding(16.dp)
            .shadow(elevation = 16.dp, clip = true)
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onImageClick()
            },
    ) {

        AsyncImage(
            modifier =
            if (showBigImage) modifier.fillMaxWidth()
            else modifier.height(200.dp),
            placeholder = painterResource(R.drawable.book_cover_placeholder),
            model = image,
            contentScale = if (showBigImage) ContentScale.FillWidth else ContentScale.FillHeight,
            contentDescription = "Book Cover Image",
        )

    }
}

@Composable
fun ThumbnailPreviewSectionItem(
    modifier: Modifier = Modifier,
    image: String?,
    selected: Boolean = false,
    onPreviewImageClick: () -> Unit
) {

    Surface(
        modifier = modifier
            .padding(4.dp)
            .clickable { onPreviewImageClick() }) {
        AsyncImage(
            modifier = modifier.height(if (!selected) 35.dp else 40.dp),
            placeholder = painterResource(R.drawable.book_cover_placeholder),
            model = image,
            alpha = if (!selected) 0.3f else 1.0f,
            contentScale = ContentScale.FillHeight,
            contentDescription = "Book Cover Image",
        )
    }
}
