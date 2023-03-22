package com.example.book_tracker.core.presentation.navigation

sealed class BookTrackerScreens(val route: String) {
    object MainActivity : BookTrackerScreens("main_screen")

    //    object SplashScreen: BookTrackerScreens("splash_screen")
    object WelcomeScreen : BookTrackerScreens("welcome_screen")

    object AuthScreen : BookTrackerScreens("auth_screen")

    object CreateProfileScreen : BookTrackerScreens("create_profile_screen")
    object ProfileScreen : BookTrackerScreens("profile_screen")

    object HomeScreen : BookTrackerScreens("home_screen")
    object SearchScreen : BookTrackerScreens("search_screen")
    object LibraryScreen : BookTrackerScreens("library_screen")

    object BookDetailSearchScreen : BookTrackerScreens("book_detail_search_screen")
    object BookDetailScreen : BookTrackerScreens("book_detail_screen")
    object BookDetailJsonScreen : BookTrackerScreens("book_detail_json_screen")
    object BookEditScreen : BookTrackerScreens("book_edit_screen")
    object BookListScreen : BookTrackerScreens("book_list_screen")
    object BookNoteListScreen : BookTrackerScreens("book_note_list_screen")
    object BookReadingListScreen : BookTrackerScreens("book_reading_list_screen")


    object UserListScreen : BookTrackerScreens("user_list_screen")
    object UserLibraryScreen : BookTrackerScreens("user_library_screen")
    object UserBookDetailScreen : BookTrackerScreens("user_book_detail_screen")
    object UserBookListScreen : BookTrackerScreens("user_book_list_screen")

    object CameraScreen : BookTrackerScreens("camera_screen")
    object StatsScreen : BookTrackerScreens("stats_screen")
    object SettingsScreen : BookTrackerScreens("settings_screen")
    object ResetPasswordScreen : BookTrackerScreens("reset_password_screen")

    companion object {

        /** userId */
        const val USER_ID = "userId"
        /** bookId */
        const val BOOK_ID = "bookId"
        /** bookShelf */
        const val BOOKSHELF = "bookShelf"

        /** ?$userId={$userId}/ */
        const val USER_ID_ARGUMENT = "?$USER_ID={$USER_ID}"
        /** ?$bookId={$bookId} */
        const val BOOK_ID_ARGUMENT = "?$BOOK_ID={$BOOK_ID}"
        /** ?$bookShelf={$bookShelf} */
        const val BOOKSHELF_ARGUMENT = "?$BOOKSHELF={$BOOKSHELF}"
    }
}