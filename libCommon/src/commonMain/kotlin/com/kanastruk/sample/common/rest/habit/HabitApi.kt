package com.kanastruk.sample.common.rest.habit

import com.kanastruk.sample.common.data.Entry
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.rest.ErrorResponse
import com.kanastruk.sample.common.rest.TypedResponse
import com.kanastruk.sample.common.rest.buildTypedResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * HabitApi gives us:
 * - Read Habits and Entries from the database.
 * - General CRuD on Habits and Entries
 *
 * The access to this data is secured per-session/anonymous user.
 * Losing your credentials means your data is lost.
 *
 * NOTE: The mock backend will be regularly reset.
 */
interface HabitApi {

    suspend fun HttpClient.getHabits(userId: String): TypedResponse<Map<String, Habit>?, ErrorResponse> {
        val path = "habit/$userId.json"
        return get(path).buildTypedResponse()
    }

    suspend fun HttpClient.getHabit(userId: String, habitId: String): TypedResponse<Habit?, ErrorResponse> {
        val path = "habit/$userId/$habitId.json"
        return get(path).buildTypedResponse()
    }

    suspend fun HttpClient.getEntries(userId: String): TypedResponse<Map<String, Map<String, Entry>>?, ErrorResponse> {
        val path = "entry/$userId.json"
        return get(path).buildTypedResponse()
    }

    suspend fun HttpClient.postHabit(userId: String, habit: Habit): TypedResponse<NameResponse, ErrorResponse> {
        val path = "habit/${userId}.json"
        val httpResponse = post(path) {
            contentType(ContentType.Application.Json)
            setBody(habit)
        }
        return httpResponse.buildTypedResponse()
    }

    suspend fun HttpClient.putHabit(userId: String, habitId: String, habit: Habit): TypedResponse<Habit, ErrorResponse> {
        val path = "habit/${userId}/${habitId}.json"
        val httpResponse = post(path) {
            contentType(ContentType.Application.Json)
            setBody(habit)
        }
        return httpResponse.buildTypedResponse()
    }

    suspend fun HttpClient.deleteHabit(userId: String, habitId: String): HttpStatusCode {
        val path = "habit/${userId}/${habitId}.json"
        val httpResponse = delete(path)
        return httpResponse.status
    }

    //    @POST("entry/{userId}/{habitId}.json")
    suspend fun HttpClient.postEntry(userId: String, habitId: String, entry: Entry): TypedResponse<NameResponse,ErrorResponse>  {
        TODO()
    }

    //    @PUT("entry/{userId}/{habitId}/{entryId}.json")
    suspend fun HttpClient.putEntry(userId: String, habitId: String, entryId: String, entry: Entry): TypedResponse<Entry,ErrorResponse> {
        TODO()
    }

    suspend fun HttpClient.deleteEntry(userId: String, habitId: String, entryId: String): HttpStatusCode {
        val path = "entry/${userId}/${habitId}/${entryId}.json"
        val httpResponse = delete(path)
        return httpResponse.status
    }

    suspend fun HttpClient.deleteEntries(userId: String, habitId: String): HttpStatusCode {
        val path = "entry/${userId}/${habitId}.json"
        val httpResponse = delete(path)
        return httpResponse.status
    }

}

class HabitApiClient(val client: HttpClient):HabitApi