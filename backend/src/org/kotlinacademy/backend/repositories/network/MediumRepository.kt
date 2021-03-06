package org.kotlinacademy.backend.repositories.network

import kotlinx.coroutines.experimental.Deferred
import org.kotlinacademy.backend.repositories.network.dto.MediumPostsResponse
import org.kotlinacademy.backend.repositories.network.dto.toArticleData
import org.kotlinacademy.common.Provider
import org.kotlinacademy.data.ArticleData
import org.kotlinacademy.fromJson
import retrofit2.http.GET
import retrofit2.http.Headers

interface MediumRepository {

    suspend fun getNews(): List<ArticleData>?

    class MediumRepositoryImpl : MediumRepository {
        private val api: Api = makeRetrofit("https://medium.com/kotlin-academy/").create(Api::class.java)

        override suspend fun getNews(): List<ArticleData>? =
                api.getPlainResponse()
                        .await()
                        // Needed because of Medium API policy https://github.com/Medium/medium-api-docs/issues/115
                        .dropWhile { it != '{' }
                        .fromJson<MediumPostsResponse>()
                        .takeIf { it != null && it.success }
                        ?.toArticleData()
    }

    interface Api {

        @Headers("Accept: application/json")
        @GET("latest?count=1000")
        fun getPlainResponse(): Deferred<String>
    }

    companion object : Provider<MediumRepository>() {
        override fun create(): MediumRepository = MediumRepositoryImpl()
    }
}