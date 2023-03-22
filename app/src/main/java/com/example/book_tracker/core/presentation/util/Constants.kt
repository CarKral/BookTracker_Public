package com.example.book_tracker.core.presentation.util

object Constants {
    const val infinityString = "∞"


    /* WorkManager Constants*/
    const val KEY_IMAGE_URI = "MY_KEY_IMAGE_URI"
    const val OUTPUT_PATH = "image_scale_outputs"
    const val TAG_OUTPUT = "OUTPUT"
    const val IMAGE_SCALE_WORK = "image_scale_work"



    // CURRENT FIREBASE USER ID for Dagger Hilt @Named Injection
    const val USER_ID = "current_firebase_user_id"

    /* Firebase COLLECTION & FIELDS constants */
    const val FIRESTORE_USERS_COLLECTION = "users"
    const val FIRESTORE_BOOKS_COLLECTION = "books"
    const val FIRESTORE_NOTES_COLLECTION = "notes"
    const val FIRESTORE_READING_COLLECTION = "reading"

    const val FIRESTORE_BOOKSHELF_FIELD = "book_shelf"
    const val FIRESTORE_ADDED_DATE_FIELD = "added_date"
    const val FIRESTORE_POSITION_FIELD = "position"
    const val FIRESTORE_BOOK_ID_FIELD = "bookId"

}

object InflectionStringConstants {
    const val INFLECTION_DAY_DEN = "den"
    const val INFLECTION_DAY_DNY = "dny"
    const val INFLECTION_DAY_DNI = "dní"

    const val INFLECTION_DAY2_DNES = "dnes"
    const val INFLECTION_DAY2_VCERA = "včera"
    const val INFLECTION_DAY2_PRED = "před"
    const val INFLECTION_DAY2_DNY = "dny"

    const val INFLECTION_PAGE_STRANA = "strana"
    const val INFLECTION_PAGE_STRANY = "strany"
    const val INFLECTION_PAGE_STRAN = "stran"

}