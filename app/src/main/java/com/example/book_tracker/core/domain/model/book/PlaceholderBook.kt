package com.example.book_tracker.core.domain.model.book

import com.google.firebase.Timestamp

/**
 * placeholderBook is a placeholder for data class Book"
 * @see Book
 * */
val placeholderBook = Book(
    id = "eLaRDs92AnCoHtv9efrK",
    title = "Pozornost",
    subtitle = "Skrytá cesta k dokonalosti",
    authors = "Daniel Goleman",
    photoUrl = "http://books.google.com/books/publisher/content?id=pdNhBAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&imgtk=AFLRE71wb11uMukM_555X3b601S5SgQacQQHmAZJjm5nWIexInZSRyi-hhp34Xz8rDODhjRxSlW61kzvwInvr_Oi53aonCO-xVUVU0-SJzNf3blBrxJl20kVy-Eb0iMuhm0VKVZ-7N2f&source=gbs_api",
    categories = "Psychology / Mental Health, Psychology / Cognitive Psychology & Cognition",
    publishedDate = "2014-09-04",
    rating = 0.0,
    pageCount = 312,
    currentPage = 210.0,
    bookShelf = "CurrentRead",
    userId = "HjDeyR9rngVTv9YZkXfiNz5SHwB3",
    googleBookId = "pdNhBAAAQBAJ",
    addedDate = Timestamp(1672581209, 0),
    // 5.1.2022
    startedReading = Timestamp(1672926809, 0),
    // 25.1.2022
    finishedReading = Timestamp(1674654809, 0),
    goalReadingSpeed = 10.0,
    // 20.1.2022
    lastReading = BookReading(
        pageRangeList = listOf(140.0, 156.0), date = Timestamp(1674222809, 0)
    )
)