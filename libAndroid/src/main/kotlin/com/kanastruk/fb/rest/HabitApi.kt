package com.kanastruk.fb.rest

import com.kanastruk.sample.common.data.Entry
import com.kanastruk.sample.common.data.Habit
import retrofit2.Response
import retrofit2.http.*

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
    @GET("habit/{userId}.json")
    suspend fun getHabits(@Path("userId") userId: String):Response<Map<String,Habit>>

    @GET("habit/{userId}/{habitId}.json")
    suspend fun getHabit(@Path("userId") userId: String, @Path("habitId") habitId: String):Response<Habit>

    @GET("entry/{userId}.json")
    suspend fun getEntries(@Path("userId") userId: String): Response<Map<String, Map<String, Entry>>>

    @POST("habit/{userId}.json")
    suspend fun postHabit(@Path("userId") userId: String, @Body habit: Habit): Response<NameResponse>

    @PUT("habit/{userId}/{habitId}.json")
    suspend fun putHabit(@Path("userId") userId: String, @Path("habitId") habitId:String, @Body habit: Habit): Response<Habit>

    @DELETE("habit/{userId}/{habitId}.json")
    suspend fun deleteHabit(@Path("userId") userId: String, @Path("habitId") habitId:String): Response<Unit>

    @POST("entry/{userId}/{habitId}.json")
    suspend fun postEntry( @Path("userId") userId: String, @Path("habitId") habitId: String, @Body entry: Entry): Response<NameResponse>

    @PUT("entry/{userId}/{habitId}/{entryId}.json")
    suspend fun putEntry( @Path("userId") userId: String, @Path("habitId") habitId: String, @Path("entryId") entryId: String, @Body entry: Entry): Response<Entry>

    @DELETE("entry/{userId}/{habitId}/{entryId}.json")
    suspend fun deleteEntry( @Path("userId") userId: String, @Path("habitId") habitId: String, @Path("entryId") entryId: String): Response<Unit>

    @DELETE("entry/{userId}/{habitId}.json")
    suspend fun deleteEntries( @Path("userId") userId: String, @Path("habitId") habitId: String): Response<Unit>

}