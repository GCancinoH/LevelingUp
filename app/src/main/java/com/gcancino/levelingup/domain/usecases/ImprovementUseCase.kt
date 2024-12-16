package com.gcancino.levelingup.domain.usecases

import com.gcancino.levelingup.data.repositories.auth.AuthRepositoryImp
import com.gcancino.levelingup.data.repositories.improvement.ImprovementRepository

class ImprovementUseCase(
    private val repository: ImprovementRepository
) {
    fun saveImprovementIntoDB(uID: String, improvements: List<String>) =
        repository.saveImprovementsToDB(uID, improvements)
}