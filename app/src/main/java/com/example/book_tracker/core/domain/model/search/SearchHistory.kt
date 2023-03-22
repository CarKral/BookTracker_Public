package com.example.book_tracker.core.domain.model.search

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class SearchHistory(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,
    var text: String? = null,
    var date: Timestamp? = Timestamp.now(),
)