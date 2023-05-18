package com.itstor.pictale.data.remote.retrofit

import com.itstor.pictale.data.remote.response.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    /**
     * Register a new user with the given name, email and password.
     * @param name The name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A call object that contains the register response.
     */
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): GeneralResponse

    /**
     * Login an existing user with the given email and password.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A call object that contains the login response.
     */
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    /**
     * Add a new story with the given description, photo and optional location.
     * The user must be authenticated with a valid token.
     * @param token The authorization token of the user.
     * @param description The description of the story.
     * @param photo The photo of the story.
     * @param lat The latitude of the story location (optional).
     * @param lon The longitude of the story location (optional).
     * @return A call object that contains the add story response.
     */
    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part("description") description: String,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): GeneralResponse

    /**
     * Add a new story as a guest with the given description, photo and optional location.
     * The user does not need to be authenticated.
     * @param description The description of the story.
     * @param photo The photo of the story.
     * @param lat The latitude of the story location (optional).
     * @param lon The longitude of the story location (optional).
     * @return A call object that contains the add story response.
     */
    @Multipart
    @POST("stories/guest")
    suspend fun addStoryGuest(
        @Part("description") description: String,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): GeneralResponse

    /**
     * Get all stories with optional pagination and location filter parameters.
     * The user must be authenticated with a valid token.
     * @param token The authorization token of the user.
     * @param page The page number to fetch (optional).
     * @param size The page size to fetch (optional).
     * @param location The location filter to apply (optional).
     * 1 for get all stories with location, 0 for get all stories without location.
     * @return A call object that contains the get all stories response.
     */
    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ): ListStoryResponse

    /**
     * Get the detail of a specific story by its id.
     * The user must be authenticated with a valid token.
     * @param token The authorization token of the user.
     * @param id The id of the story to fetch.
     * @return A call object that contains the get detail story response.
     */
    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse
}

