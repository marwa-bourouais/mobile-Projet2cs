/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.waelkhelil.sayara_dz.database

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.waelkhelil.sayara_dz.database.data.BrandsRepository
import com.waelkhelil.sayara_dz.database.db.AppDb
import com.waelkhelil.sayara_dz.database.db.LocalCache
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [LocalCache] based on the database DAO.
     */
     fun provideCache(context: Context): LocalCache {
        val database = AppDb.getInstance(context)
        return LocalCache(database.brandsDao(), database.modelsDao(),Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [BrandRepository] based on the [SayaraDZService] and a
     * [LocalCache]
     */
    private fun provideBrandRepository(context: Context): BrandsRepository {
        return BrandsRepository()
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideBrandRepository(context))
    }
    fun provideModelViewModelFactory(id:String): ViewModelProvider.Factory {
        return ModelViewModelFactory(id)
    }

}