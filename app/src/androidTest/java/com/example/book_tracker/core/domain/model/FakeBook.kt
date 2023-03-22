package com.example.book_tracker.core.domain.model

import com.example.book_tracker.core.domain.model.book.*
import com.google.firebase.Timestamp

val fakeBook = Book(
    id = "eLaRDs92AnCoHtv9efrK",
    title = "Pozornost",
    subtitle = "Skrytá cesta k dokonalosti",
    authors = "Daniel Goleman",
    categories = "Psychology / Mental Health, Psychology / Cognitive Psychology & Cognition",
    photoUrl = "http://books.google.com/books/publisher/content?id=pdNhBAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&imgtk=AFLRE71wb11uMukM_555X3b601S5SgQacQQHmAZJjm5nWIexInZSRyi-hhp34Xz8rDODhjRxSlW61kzvwInvr_Oi53aonCO-xVUVU0-SJzNf3blBrxJl20kVy-Eb0iMuhm0VKVZ-7N2f&source=gbs_api",
    publishedDate = "2014-09-04",
    publisher = "Jan Melvil Publishing",
    addedDate = Timestamp(1674130530, 332000000),
    rating = 5.0,
    pageCount = 312,
    currentPage = null,
    bookShelf = "Unclassified",
    printType = listOf(BookPrintType.Book.toString()),
    startedReading = null,
    finishedReading = null,
    userId = "HjDeyR9rngVTv9YZkXfiNz5SHwB3",
    googleBookId = "pdNhBAAAQBAJ"
)

val fakeItem = Item(
    kind = "books#volume",
    id = "dE2qEAAAQBAJ",
    etag = "b+ElJ/YhhNw",
    selfLink = "https://content-books.googleapis.com/books/v1/volumes/dE2qEAAAQBAJ",
    volumeInfo = VolumeInfo(
        title = "Geniální potraviny",
        subtitle = "Jídlem proti špatné náladě, únavě, mozkové mlze a demenci",
        authors = listOf("Max Lugavere", "Paul Grewal"),
        publisher = "Jan Melvil Publishing",
        publishedDate = "2023-01-31",
        description = "\u003e\u003e Na seznamu bestsellerů New York Times \u003c\u003c \u003e\u003e „Tohle není další kniha, podle které stačí jíst vejce, ořechy a ryby, a hned budete chytřejší. Geniální potraviny vysvětlují, které geny ovlivňují naši inteligenci a jakým jídlem z nich dostat nejvíc. Přinášejí lahodné recepty a k tomu super jednoduché nápady, co v životě změnit, aby vám to lépe myslelo. Povinná četba.“ – Ben Greenfield, zdravotní konzultant a spisovatel \u003c\u003c ## O knize Kručí vám v mozku? Začněte ho krmit pořádně! Olivový olej, avokádo, borůvky, hořká čokoláda, vejce, divoký losos, hovězí z pastvy, tmavá listová zelenina, brokolice, mandle. Seznam deseti geniálních potravin není tajemstvím, tak proč ho neprozradit rovnou. V knize se toho totiž dozvíte mnohem víc: - Čím přesně jsou pro vás geniální potraviny prospěšné a proč vám právě ony pomůžou k lepší duševní i tělesné kondici. - Jak úpravou jídelníčku ztlumit příznaky ADHD, úzkostí a depresí a snížit riziko demence a Alzheimerovy i Parkinsonovy choroby nebo odbourat tuky tzv. biochemickou liposukcí. - Že jsme ve srovnání s našimi předky fyzicky značně zchátrali. Ti totiž běžně mívali kondici vrcholových sportovců. - Jaké jsou příčiny mozkové mlhy a únavy a jak se jich zbavit. - Že stárneme tak rychle, jak rychle produkujeme inzulin - a proto se musíme o jeho uvolňování začít pečlivě starat. ## Díky Geniálnímu plánu vyladíte svůj mozek i tělo a budete mít jasno v tom: - Kdy jíst a kdy už raději ne, chcete-li zůstat bystří celý den a v noci dobře spát. - Že některé potraviny možná bleskově zlepšují náladu, ale dlouhodobý prospěch obvykle přinášejí jiné. - Které změny životního stylu vaše zdraví posílí bezbolestně a co vám naopak za trápení vůbec nestojí. - Že sice neexistuje návod na zázračně rychlé zhubnutí, ale můžete významně pomoci tomu, abyste zůstali celý život fyzicky i psychicky fit. ## O autorovi Max Lugavere (* 1982) je americký publicista, dokumentarista a spisovatel, který se specializuje na zdraví a racionální výživu. Absolvoval Univerzitu v Miami. Pro knihu Geniální potraviny porovnal stovky vědeckých studií a konzultoval ji s desítkami odborníků z celého světa. # Více o knize http://www.melvil.cz/kniha-genialni-potraviny",
        industryIdentifiers = listOf(
            IndustryIdentifier(type = "ISBN_13", identifier = "9788075551801"),
            IndustryIdentifier(type = "ISBN_10", identifier = "807555180X")
        ),
        readingModes = ReadingModes(text = true, image = false),
        pageCount = 339,
        printType = "BOOK",
        categories = listOf(
            "Health & Fitness"
        ),
        maturityRating = "NOT_MATURE",
        allowAnonLogging = false,
        contentVersion = "1.1.1.0.preview.2",

        imageLinks = ImageLinks(
            smallThumbnail = "http://books.google.com/books/content?id=dE2qEAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
            thumbnail = "http://books.google.com/books/content?id=dE2qEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
            extraLarge = null, small = null, large = null, medium = null
        ),
        language = "cs",
        previewLink = "http://books.google.cz/books?id=dE2qEAAAQBAJ&pg=PT28&dq=geni%C3%A1ln%C3%AD+potraviny&hl=&cd=1&source=gbs_api",
        infoLink = "https://play.google.com/store/books/details?id=dE2qEAAAQBAJ&source=gbs_api",
        canonicalVolumeLink = "https://play.google.com/store/books/details?id=dE2qEAAAQBAJ",
        averageRating = null,
        dimensions = null,
        mainCategory = null,
        ratingsCount = null,
        panelizationSummary = null,
    ),
    saleInfo = SaleInfo(
        country = "CZ",
        saleability = "FOR_SALE",
        isEbook = true,
        listPrice = ListPrice(
            amount = 398.82,
            currencyCode = "CZK"
        ),
        retailPrice = RetailPriceX(
            amount = 339.0,
            currencyCode = "CZK"
        ),
        buyLink = "https://play.google.com/store/books/details?id=dE2qEAAAQBAJ&rdid=book-dE2qEAAAQBAJ&rdot=1&source=gbs_api",
        offers = listOf(
            Offer(
                finskyOfferType = 1,
                listPrice = ListPriceX(
                    amountInMicros = 398820000,
                    currencyCode = "CZK"
                ),
                giftable = null,
                retailPrice = null
            ), Offer(
                retailPrice = RetailPrice(
                    amountInMicros = 339000000,
                    currencyCode = "CZK"
                ),
                giftable = null,
                finskyOfferType = null,
                listPrice = null,
            )
        )
    ),
    accessInfo = AccessInfo(
        country = "CZ",
        viewability = "PARTIAL",
        embeddable = true,
        publicDomain = false,
        textToSpeechPermission = "ALLOWED",
        epub = Epub(
            isAvailable = true,
            acsTokenLink = "http://books.google.cz/books/download/Geni%C3%A1ln%C3%AD_potraviny-sample-epub.acsm?id=dE2qEAAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
        ),
        accessViewStatus = "SAMPLE",
        quoteSharingAllowed = false,
        pdf = Pdf(isAvailable = false, acsTokenLink = null),
        webReaderLink = "http://play.google.com/books/reader?id=dE2qEAAAQBAJ&hl=&source=gbs_api",
    ),
    searchInfo = SearchInfo(
        textSnippet = "ZNAČKA: FRANKENSTEINOVY \u003cb\u003ePOTRAVINY\u003c/b\u003e Nakolik lze \u003cb\u003epotravinami\u003c/b\u003e manipulovat, abychom je ještě nemuseli přestat nazývat \u003cb\u003epotravinami\u003c/b\u003e? Po mnoho let se musely výrobky, které nesplňovaly přísnou definici základních \u003cb\u003epotravin\u003c/b\u003e, ve Spojených státech&nbsp;..."
    )
)
