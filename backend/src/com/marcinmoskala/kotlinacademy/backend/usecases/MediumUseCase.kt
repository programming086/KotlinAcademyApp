package com.marcinmoskala.kotlinacademy.backend.usecases

import com.marcinmoskala.kotlinacademy.backend.logInfo
import com.marcinmoskala.kotlinacademy.backend.repositories.db.DatabaseRepository
import com.marcinmoskala.kotlinacademy.backend.repositories.network.MediumRepository
import com.marcinmoskala.kotlinacademy.backend.repositories.network.NotificationsRepository

suspend fun syncWithMedium(mediumRepository: MediumRepository, databaseRepository: DatabaseRepository, notificationsRepository: NotificationsRepository?) {
    val news = mediumRepository.getNews()
    if (news == null) {
        logInfo("Medium did not succeed when processing request")
        return
    }

    val prevNewsTitles = databaseRepository.getNews().map { it.title }
    news.filter { it.title !in prevNewsTitles }
            .forEach { addNews(it, databaseRepository, notificationsRepository) }
}