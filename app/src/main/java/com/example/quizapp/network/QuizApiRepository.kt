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
import com.example.quizapp.model.StudySet
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Token
import com.example.quizapp.network.request_model.LoginRequest
import com.example.quizapp.network.response_model.ApiResponse


/**
 * Repository that fetch mars photos list from marsApi.
 */
interface QuizApiRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun login(loginRequest: LoginRequest): ApiResponse<Token>

    suspend fun getCourses(): ApiResponse<List<Course>>
    suspend fun getStudySets(): ApiResponse<List<StudySet>>
    suspend fun getStudySet(id: Int): ApiResponse<StudySetDetail>
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

    override suspend fun getCourses(): ApiResponse<List<Course>> =
        handleApi { quizApiService.getCourses() }

    override suspend fun getStudySets(): ApiResponse<List<StudySet>> =
        handleApi { quizApiService.getStudySets() }

    override suspend fun getStudySet(id: Int): ApiResponse<StudySetDetail> {
        return handleApi { quizApiService.getStudySet(id) }
    }
}
