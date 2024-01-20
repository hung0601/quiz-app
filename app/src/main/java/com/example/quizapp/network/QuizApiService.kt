/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.quizapp.network

import com.example.quizapp.model.Course
import com.example.quizapp.model.CourseDetail
import com.example.quizapp.model.CourseInvite
import com.example.quizapp.model.Profile
import com.example.quizapp.model.Search
import com.example.quizapp.model.StudySet
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.model.Token
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A public interface that exposes the [getStudySets] method
 */
interface QuizApiService {
    /**
     * Returns a [List] of [StudySet] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "photos" endpoint will be requested with the GET
     * HTTP method
     */
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Token>


    @GET("user/top_creators")
    suspend fun getTopCreators(): Response<List<Profile>>

    @GET("courses")
    suspend fun getCourses(): Response<List<Course>>

    @GET("study_sets")
    suspend fun getStudySets(): Response<List<StudySet>>

    @GET("study_sets/created_sets")
    suspend fun getCreatedSets(): Response<List<StudySet>>

    @GET("study_sets/{id}")
    suspend fun getStudySet(@Path("id") setId: Int): Response<StudySetDetail>

    @GET("courses/{id}")
    suspend fun getCourse(@Path("id") setId: Int): Response<CourseDetail>

    @FormUrlEncoded
    @POST("courses")
    suspend fun createCourse(
        @Field("title") title: String,
        @Field("description") description: String
    ): Response<Course>


    @POST("study_sets")
    suspend fun createSet(@Body body: RequestBody): Response<StudySet>

    @POST("terms")
    suspend fun addTerm(@Body body: RequestBody): Response<Term>

    @FormUrlEncoded
    @POST("courses/add_study_set")
    suspend fun addStudySets(
        @Field("course_id") courseId: Int,
        @Field("study_set_ids[]") studySetIds: List<Int>
    ): Response<Unit>


    @FormUrlEncoded
    @POST("courses/invite_member")
    suspend fun inviteMember(
        @Field("course_id") courseId: Int,
        @Field("participant_id") participantId: Int,
        @Field("type") type: String,
    ): Response<Unit>

    @FormUrlEncoded
    @POST("courses/accept_invite")
    suspend fun acceptInvite(
        @Field("course_id") courseId: Int,
        @Field("isAccept") isAccept: Boolean,
    ): Response<Boolean>


    @GET("search_users")
    suspend fun searchCourseUser(
        @Query("course_id") courseId: Int,
        @Query("search") search: String,
    ): Response<List<Profile>>

    @GET("search")
    suspend fun search(
        @Query("search") search: String,
    ): Response<Search>

    @GET("get_invites")
    suspend fun getInvites(): Response<List<CourseInvite>>
}
