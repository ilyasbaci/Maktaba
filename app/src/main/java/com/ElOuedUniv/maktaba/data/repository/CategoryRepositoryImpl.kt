package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Category

class CategoryRepositoryImpl : CategoryRepository {

    private val categoriesList = listOf(
        Category(
            id = "1",
            name = "Programming",
            description = "Books related to programming languages"
        ),
        Category(
            id = "2",
            name = "Software Engineering",
            description = "Books about software design and architecture"
        ),
        Category(
            id = "3",
            name = "Databases",
            description = "Books about database systems and SQL"
        ),
        Category(
            id = "4",
            name = "Artificial Intelligence",
            description = "Books about AI and machine learning"
        ),
        Category(
            id = "5",
            name = "Mobile Development",
            description = "Books about Android development"
        )
    )

    override fun getAllCategories(): List<Category> {
        return categoriesList
    }

    override fun getCategoryById(id: String): Category? {
        return categoriesList.find { it.id == id }
    }
}