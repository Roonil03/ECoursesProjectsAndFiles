package com.littlelemon.menu

class SortHelper {

    fun sortProducts(type: SortType, productsList: List<ProductItem>): List<ProductItem> {
        return when (type) {
            SortType.Alphabetically -> productsList.sortAlphabetically()
            SortType.PriceAsc -> productsList.sortByPriceAscending()
            SortType.PriceDesc -> productsList.sortByPriceDescending()
        }
    }

    private fun List<ProductItem>.sortAlphabetically(): List<ProductItem> {
        return sortedBy { it.title }
    }

    fun List<ProductItem>.sortByPriceAscending(): List<ProductItem> {
        return this.sortedBy { it.price }
    }

    fun List<ProductItem>.sortByPriceDescending(): List<ProductItem> {
        return this.sortedByDescending { it.price }
    }
}