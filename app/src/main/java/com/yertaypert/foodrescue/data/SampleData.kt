package com.yertaypert.foodrescue.data

object SampleData {
    val listings: List<Listing> = listOf(
        Listing(
            1, "Mixed pastries", "Golden Crumb", 0.4, Category.Bakery, 18,
            "Croissants, buns and danishes from today's batch."
        ),
        Listing(
            2, "Lunch leftovers", "Cafe Meridian", 2.0, Category.Meals, 6,
            "Pasta and salads, packed in boxes."
        ),
        Listing(
            3, "Veg box, 3 portions", "Green Grocer Co.", 1.1, Category.Produce, 120,
            "Seasonal vegetables that didn't make the shelf."
        ),
        Listing(
            4, "Unsold pastries + drinks", "Brew & Co", 1.6, Category.Drinks, 45,
            "A bag of pastries plus two bottled drinks."
        ),
        Listing(
            5,
            "Yogurt and kefir, close to best-before date",
            "Dairy Lane",
            0.9,
            Category.Dairy,
            240,
            "Sealed packs, best before tomorrow."
        ),
        Listing(
            6, "Sourdough loaves", "Rye & Co", 1.8, Category.Bakery, 30,
            "Two sourdough loaves left from the morning."
        ),
        Listing(
            7,
            "Apples and pears surplus from the weekend farmers market stall",
            "Orchard Stand",
            2.4,
            Category.Produce,
            90,
            "About 3 kg of mixed fruit, slightly bruised but tasty."
        ),
        Listing(
            8, "Chicken soup, 4 servings", "Home Kitchen", 0.7, Category.Meals, 60,
            "Homemade soup in a sealed container."
        ),
        Listing(
            9, "Cold brew bottles", "Brew & Co", 1.6, Category.Drinks, 15,
            "Three bottles of cold brew."
        ),
        Listing(
            10, "Cottage cheese", "Dairy Lane", 0.9, Category.Dairy, 180,
            "Four 200 g packs."
        ),
        Listing(
            11, "Bagels, dozen", "Golden Crumb", 0.4, Category.Bakery, 25,
            "A dozen plain and sesame bagels."
        ),
        Listing(
            12, "Salad bowls", "Cafe Meridian", 2.0, Category.Meals, 10,
            "Fresh salad bowls, no dressing."
        ),
        Listing(
            13, "Tomatoes and cucumbers", "Green Grocer Co.", 1.1, Category.Produce, 300,
            "Mixed box of salad vegetables."
        )
    )

    fun byId(id: Int): Listing? = listings.firstOrNull { it.id == id }
}