package com.hrishikesh.stockmarketapp.data.repository

import com.hrishikesh.stockmarketapp.data.local.StockDatabase
import com.hrishikesh.stockmarketapp.data.mapper.toCompanyListing
import com.hrishikesh.stockmarketapp.data.remote.dto.StockApi
import com.hrishikesh.stockmarketapp.domain.model.CompanyListing
import com.hrishikesh.stockmarketapp.domain.repository.StockRepository
import com.hrishikesh.stockmarketapp.util.Resource
import com.opencsv.CSVReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api : StockApi,
    val db : StockDatabase
) : StockRepository{

    private val dao = db.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow{
            emit(Resource.Loading(true))
            val localListing = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListing.map { it.toCompanyListing() }
            ))
            val isDbEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try{
                val response = api.getListing()

            }catch (e : IOException){
                e.printStackTrace()
                emit(Resource.Error("couldn't load data from IOException"))
            }catch (e : HttpException){
                e.printStackTrace()
                emit(Resource.Error("couldn't load data from network fail"))
            }
        }
    }

}