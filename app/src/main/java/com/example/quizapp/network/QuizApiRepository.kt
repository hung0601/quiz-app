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
import com.example.quizapp.model.CreatorProfile
import com.example.quizapp.model.ExamDetail
import com.example.quizapp.model.Member
import com.example.quizapp.model.MyProfile
import com.example.quizapp.model.Search
import com.example.quizapp.model.StudySet
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.model.Token
import com.example.quizapp.network.request_model.LoginRequest
import com.example.quizapp.network.request_model.StoreStudyRequest
import com.example.quizapp.network.request_model.SubmitExamRequest
import com.example.quizapp.network.response_model.ApiResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


/**
 * Repository that fetch mars photos list from marsApi.
 */
interface QuizApiRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun login(loginRequest: LoginRequest): ApiResponse<Token>

    suspend fun getTopCreators(): ApiResponse<List<MyProfile>>
    suspend fun getMyProfile(): ApiResponse<MyProfile>
    suspend fun getProfile(userId: Int): ApiResponse<CreatorProfile>
    suspend fun getSetsByUser(userId: Int): ApiResponse<List<StudySet>>
    suspend fun getCoursesByUser(userId: Int): ApiResponse<List<Course>>
    suspend fun getFollowers(userId: Int? = null): ApiResponse<List<CreatorProfile>>
    suspend fun getCourses(): ApiResponse<List<Course>>
    suspend fun getStudySets(): ApiResponse<List<StudySet>>
    suspend fun getCreatedSets(): ApiResponse<List<StudySet>>
    suspend fun getStudySet(id: Int): ApiResponse<StudySetDetail>
    suspend fun getCourse(id: Int): ApiResponse<CourseDetail>

    suspend fun createCourse(title: String, description: String): ApiResponse<Course>
    suspend fun addStudySets(courseId: Int, studySetIds: List<Int>): ApiResponse<Unit>
    suspend fun acceptInvite(courseId: Int, isAccept: Boolean): ApiResponse<Boolean>
    suspend fun getInvites(): ApiResponse<List<CourseInvite>>
    suspend fun createSet(
        image: File,
        title: String,
        description: String,
        courseId: Int?
    ): ApiResponse<StudySet>

    suspend fun addTerm(
        image: File,
        term: String,
        definition: String,
        studySetId: Int
    ): ApiResponse<Term>

    suspend fun inviteMember(
        courseId: Int,
        participantId: Int,
        type: String
    ): ApiResponse<Unit>

    suspend fun searchCourseUsers(courseId: Int, search: String): ApiResponse<List<MyProfile>>
    suspend fun search(search: String): ApiResponse<Search>
    suspend fun storeStudyResults(results: List<StoreStudyRequest>): ApiResponse<Unit>
    suspend fun getExam(id: Int): ApiResponse<ExamDetail>
    suspend fun voteSet(studySetId: Int, star: Int): ApiResponse<Float>
    suspend fun submitExam(examId: Int, results: List<SubmitExamRequest>): ApiResponse<Unit>
    suspend fun followUser(userId: Int): ApiResponse<Unit>
    suspend fun unFollow(userId: Int): ApiResponse<Unit>
    suspend fun getStudySetMembers(id: Int): ApiResponse<List<Member>>
    suspend fun inviteToSet(setId: Int, userId: Int, accessLevel: Int): ApiResponse<Unit>
    suspend fun removeSetMember(setId: Int, userId: Int): ApiResponse<Unit>
    suspend fun leaveSetMember(setId: Int): ApiResponse<Unit>
    suspend fun searchSetUsers(setId: Int, search: String): ApiResponse<List<MyProfile>>
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class NetworkQuizApiRepository(
    private val quizApiService: QuizApiService
) : QuizApiRepository, ApiHandler {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun login(loginRequest: LoginRequest): ApiResponse<Token> {
        return handleApi { quizApiService.login(loginRequest.email, loginRequest.password) }
    }

    override suspend fun getTopCreators(): ApiResponse<List<MyProfile>> =
        handleApi { quizApiService.getTopCreators() }

    override suspend fun getMyProfile(): ApiResponse<MyProfile> {
        return handleApi { quizApiService.getMyProfile() }
    }

    override suspend fun getSetsByUser(userId: Int): ApiResponse<List<StudySet>> {
        return handleApi { quizApiService.getSetsByUser(userId) }
    }

    override suspend fun getCoursesByUser(userId: Int): ApiResponse<List<Course>> {
        return handleApi { quizApiService.getCoursesByUser(userId) }
    }

    override suspend fun getFollowers(userId: Int?): ApiResponse<List<CreatorProfile>> {
        return handleApi { quizApiService.getFollowers(userId) }
    }

    override suspend fun getProfile(userId: Int): ApiResponse<CreatorProfile> {
        return handleApi { quizApiService.getProfile(userId) }
    }

    override suspend fun getCourses(): ApiResponse<List<Course>> =
        handleApi { quizApiService.getCourses() }

    override suspend fun getStudySets(): ApiResponse<List<StudySet>> =
        handleApi { quizApiService.getStudySets() }

    override suspend fun getCreatedSets(): ApiResponse<List<StudySet>> =
        handleApi { quizApiService.getCreatedSets() }

    override suspend fun getStudySet(id: Int): ApiResponse<StudySetDetail> {
        return handleApi { quizApiService.getStudySet(id) }
    }

    override suspend fun getCourse(id: Int): ApiResponse<CourseDetail> {
        return handleApi { quizApiService.getCourse(id) }
    }

    override suspend fun createCourse(title: String, description: String): ApiResponse<Course> {
        return handleApi {
            quizApiService.createCourse(title, description)
        }
    }

    override suspend fun addStudySets(courseId: Int, studySetIds: List<Int>): ApiResponse<Unit> {
        return handleApi {
            quizApiService.addStudySets(courseId, studySetIds)
        }
    }

    override suspend fun acceptInvite(courseId: Int, isAccept: Boolean): ApiResponse<Boolean> {
        return handleApi {
            quizApiService.acceptInvite(courseId, isAccept)
        }
    }

    override suspend fun getInvites(): ApiResponse<List<CourseInvite>> {
        return handleApi {
            quizApiService.getInvites()
        }
    }

    override suspend fun createSet(
        image: File,
        title: String,
        description: String,
        courseId: Int?
    ): ApiResponse<StudySet> {
        return handleApi {
            val builder: MultipartBody.Builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", title)
                .addFormDataPart("description", description)
                .addFormDataPart(
                    "image",
                    image.name,
                    image.asRequestBody("image/*".toMediaTypeOrNull())
                )
            if (courseId != null) builder.addFormDataPart("course_id", courseId.toString())
            val requestBody = builder.build()
            quizApiService.createSet(requestBody)
        }
    }

    override suspend fun addTerm(
        image: File,
        term: String,
        definition: String,
        studySetId: Int
    ): ApiResponse<Term> {
        return handleApi {
            val builder: MultipartBody.Builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("term", term)
                .addFormDataPart("definition", definition)
                .addFormDataPart("study_set_id", studySetId.toString())
                .addFormDataPart(
                    "image",
                    image.name,
                    image.asRequestBody("image/*".toMediaTypeOrNull())

                )
            val requestBody = builder.build()
            quizApiService.addTerm(requestBody)
        }
    }

    override suspend fun inviteMember(
        courseId: Int,
        participantId: Int,
        type: String
    ): ApiResponse<Unit> {
        return handleApi {
            quizApiService.inviteMember(courseId, participantId, type)
        }
    }

    override suspend fun searchCourseUsers(
        courseId: Int,
        search: String
    ): ApiResponse<List<MyProfile>> {
        return handleApi {
            quizApiService.searchCourseUser(courseId, search)
        }
    }

    override suspend fun search(search: String): ApiResponse<Search> {
        return handleApi {
            quizApiService.search(search)
        }
    }

    override suspend fun storeStudyResults(results: List<StoreStudyRequest>): ApiResponse<Unit> {
        return handleApi {
            quizApiService.storeStudyResults(results)
        }
    }

    override suspend fun getExam(id: Int): ApiResponse<ExamDetail> {
        return handleApi {
            quizApiService.getExam(id)
        }
    }

    override suspend fun voteSet(studySetId: Int, star: Int): ApiResponse<Float> {
        return handleApi {
            quizApiService.voteSet(studySetId, star)
        }
    }

    override suspend fun submitExam(
        examId: Int,
        results: List<SubmitExamRequest>
    ): ApiResponse<Unit> {
        return handleApi {
            quizApiService.submitExam(examId, results)
        }
    }

    override suspend fun followUser(userId: Int): ApiResponse<Unit> {
        return handleApi {
            quizApiService.followUser(userId)
        }
    }

    override suspend fun unFollow(userId: Int): ApiResponse<Unit> {
        return handleApi {
            quizApiService.unFollow(userId)
        }
    }

    override suspend fun getStudySetMembers(id: Int): ApiResponse<List<Member>> {
        return handleApi {
            quizApiService.getStudySetMembers(id)
        }
    }

    override suspend fun inviteToSet(setId: Int, userId: Int, accessLevel: Int): ApiResponse<Unit> {
        return handleApi {
            quizApiService.inviteToSet(setId, userId, accessLevel)
        }
    }

    override suspend fun removeSetMember(setId: Int, userId: Int): ApiResponse<Unit> {
        return handleApi {
            quizApiService.removeSetMember(setId, userId)
        }
    }

    override suspend fun leaveSetMember(setId: Int): ApiResponse<Unit> {
        return handleApi {
            quizApiService.leaveSetMember(setId)
        }
    }

    override suspend fun searchSetUsers(setId: Int, search: String): ApiResponse<List<MyProfile>> {
        return handleApi {
            quizApiService.searchSetUsers(setId, search)
        }
    }
}
