package com.example.book_tracker.core.domain.model.book

/** Book is a data class for Google Books data about book */
data class ItemsResponse(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)

/** Item is a data class for Book (Google Books data)
 * @see ItemsResponse
 * */
data class Item(
    val accessInfo: AccessInfo? = null,
    val etag: String? = null,
    val id: String,
    val kind: String? = null,
    val saleInfo: SaleInfo? = null,
    val searchInfo: SearchInfo? = null,
    val selfLink: String? = null,
    val volumeInfo: VolumeInfo? = null
)

/** AccessInfo is a data class for Item
 * @see Item
 * */
data class AccessInfo(
    val accessViewStatus: String?,
    val country: String?,
    val embeddable: Boolean?,
    val epub: Epub?,
    val pdf: Pdf?,
    val publicDomain: Boolean?,
    val quoteSharingAllowed: Boolean?,
    val textToSpeechPermission: String?,
    val viewability: String?,
    val webReaderLink: String?,
)

/** Pdf is a data class for AccessInfo
 * @see AccessInfo
 * */
data class Pdf(val acsTokenLink: String?, val isAvailable: Boolean?)

/** Epub is a data class for AccessInfo
 * @see AccessInfo
 * */
data class Epub(val acsTokenLink: String?, val isAvailable: Boolean?)


/** SearchInfo is a data class for Item
 * @see Item
 * */
data class SearchInfo(val textSnippet: String?)

/** VolumeInfo is a data class for Item
 * @see Item
 * */
data class VolumeInfo(
    val allowAnonLogging: Boolean?,
    val authors: List<String>?,
    val averageRating: Double?,
    val canonicalVolumeLink: String?,
    val categories: List<String>?,
    val mainCategory: String?,
    val contentVersion: String?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val industryIdentifiers: List<IndustryIdentifier>?,
    val infoLink: String?,
    val language: String?,
    val maturityRating: String?,
    val pageCount: Int?,
    val dimensions: Dimensions?,
    val panelizationSummary: PanelizationSummary?,
    val previewLink: String?,
    val printType: String?,
    val publishedDate: String?,
    val publisher: String?,
    val ratingsCount: Int?,
    val readingModes: ReadingModes?,
    val subtitle: String?,
    val title: String?
)

/** ImageLinks is a data class for VolumeInfo
 * @see VolumeInfo
 * */
data class ImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?,
    val small: String?,
    val medium: String?,
    val large: String?,
    val extraLarge: String?,
)

/** IndustryIdentifier is a data class for VolumeInfo
 * @see VolumeInfo
 * */
data class IndustryIdentifier(val identifier: String?, val type: String?)

/** Dimensions is a data class for VolumeInfo
 * @see VolumeInfo
 * */
data class Dimensions(val height: String?, val width: String?, val thickness: String?, )

/** PanelizationSummary is a data class for VolumeInfo
 * @see VolumeInfo
 * */
data class PanelizationSummary(val containsEpubBubbles: Boolean?, val containsImageBubbles: Boolean?)

/** ReadingModes is a data class for VolumeInfo
 * @see VolumeInfo
 * */
data class ReadingModes(
    val image: Boolean?,
    val text: Boolean?
)


/** SaleInfo is a data class for Item
 * @see Item
 * */
data class SaleInfo(
    val buyLink: String?,
    val country: String?,
    val isEbook: Boolean?,
    val listPrice: ListPrice?,
    val offers: List<Offer>?,
    val retailPrice: RetailPriceX?,
    val saleability: String?,
)

/** ListPrice is a data class for SaleInfo
 * @see SaleInfo
 * */
data class ListPrice(
    val amount: Double?,
    val currencyCode: String?
)

/** RetailPriceX is a data class for SaleInfo
 * @see SaleInfo
 * */
data class RetailPriceX(
    val amount: Double?,
    val currencyCode: String?
)

/** Offer is a data class for SaleInfo
 * @see SaleInfo
 * */
data class Offer(
    val finskyOfferType: Int?,
    val giftable: Boolean?,
    val listPrice: ListPriceX?,
    val retailPrice: RetailPrice?,
)

/** ListPriceX is a data class for Offer
 * @see Offer
 * */
data class ListPriceX(
    val amountInMicros: Int?,
    val currencyCode: String?
)

/** RetailPrice is a data class for Offer
 * @see Offer
 * */
data class RetailPrice(
    val amountInMicros: Int?,
    val currencyCode: String?
)