package com.waelkhelil.sayara_dz.view.model_ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.waelkhelil.sayara_dz.database.api.SayaraDzService
import com.waelkhelil.sayara_dz.database.data.BrandsRepository
import com.waelkhelil.sayara_dz.database.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class ModelVersionsViewModel (var id_modele:String) : ViewModel() {

    private var versionsList: MutableLiveData<List<Version>>? = null
    private var carsList: MutableLiveData<List<Vehicule>> = MutableLiveData<List<Vehicule>>()
    private var Repository: BrandsRepository? = null
    // LiveData of network errors.
    private var orderMessage = MutableLiveData<String>()
    private var networkErrors = MutableLiveData<String>()


    fun init() {
        if (versionsList != null) {
            return
        }
        Repository = BrandsRepository.instance
        versionsList = Repository!!.getVersions(id_modele, { error ->
            networkErrors!!.postValue(error)})


    }

    fun getVersionsList(): LiveData<List<Version>>? {
        return versionsList
    }
    fun getNetworkErrors(): LiveData<String>? {
        return networkErrors

    }
    fun getOrderMessage(): LiveData<String>? {
        return orderMessage

    }

    fun getVersionPrice(id_version:String):MutableLiveData<List<VersionPrice>>
    {
        return Repository!!.getVersionPrice(id_version,{error ->
            networkErrors!!.postValue(error)})
    }
    fun getColorPrice(id_color:String): MutableLiveData<List<ColorPrice>>
    {
        return Repository!!.getColorPrice(id_color,{ error ->
            networkErrors!!.postValue(error)})
    }
    fun getOptionPrice(id_option:String): MutableLiveData<List<OptionPrice>>
    {
        return Repository!!.getOptionPrice(id_option,{error ->
            networkErrors!!.postValue(error)})
    }

    fun getVersionOptions(id_version:String):MutableLiveData<List<Option>>
    {
        return Repository!!.getVersionOptions(id_version,{error ->
            networkErrors!!.postValue(error)})
    }








    fun reserver (user_email:String,car_id:String,price:Float):MutableLiveData<String>
        {
            var result :MutableLiveData<String> = MutableLiveData("")
            // pick current system date
            var dt:Date  = Date();

            // set format for date
            var dateFormat :SimpleDateFormat  =  SimpleDateFormat("yyyy-MM-dd");

            // parse it like
            var current_date:String = dateFormat.format(dt);


        val paramObject = JsonObject()
            paramObject.addProperty( "automobiliste", user_email)
            paramObject.addProperty( "vehicule", car_id)
            paramObject.addProperty( "date",current_date)
            paramObject.addProperty( "montant",price)
            paramObject.addProperty(  "reserver", true)
    


             SayaraDzService.create().sendReservation(paramObject).
                enqueue(
                    object : Callback<reservation> {
                        override fun onResponse(call: Call<reservation>, response: Response<reservation>) {

                            if (response.isSuccessful()) {
                                result.postValue("votre demande a bien été envoyée vous serez notifiés pour" +
                                        "une eventuelle confirmation")
                                Log.i("info","responded")
                            } else {


                                if (response.code() == 400) {
                                    Log.v("Error code 400",response.errorBody()!!.string())
                                }

                            }
                        }
                        override fun onFailure(call: Call<reservation>, t: Throwable) {

                        }


    })
                  return result
        }



     fun checkAvailable (brand_id:String,modele_id:String,version_id:String,color_id:String,options:List<String>,
                         price:Float):MutableLiveData<String>
    {  var result :MutableLiveData<String> = MutableLiveData("")
        var found:Boolean = false
        SayaraDzService.create().checkAvailable().enqueue(
                object : Callback<List<Vehicule>> {
                    override fun onResponse(call: Call<List<Vehicule>>, response: Response<List<Vehicule>>) {
                        if (response.isSuccessful()) {
                            if (response.body()!!.size!=0)
                            {

                                carsList!!.value=response.body()

                                var i=carsList!!.value!!.size
                                var j:Int=0
                                var item:Vehicule?
                                while (j<i && !found)
                                {   item= carsList.value!![j]
                                    if (( item.brand_id ==brand_id)&&(item.color_id==color_id)&&(item.modele_id==modele_id)
                                        &&(item.version_id==version_id)

                                        && optionsEqual(item.options,options))
                                    {
                                        //reserver("fm_bourouais@esi.dz",item.car_id,price)
                                        Log.i("in",optionsEqual(item.options,options).toString())
                                        result.postValue(item.car_id)
                                        found=true

                                    }

                                    j++
                                }





                            }

                        } else {
                            //TODO: fix prblm here of order_error
                           //orderMessage.postValue(Resources.getSystem().getString(R.string.order_error))





                        }
                    }
                    override fun onFailure(call: Call<List<Vehicule>>, t: Throwable) {

                        //orderMessage.postValue(Resources.getSystem().getString(R.string.order_error))
                    }




                })
                  return result


    }



    fun optionsEqual (carOptions: String,neededOptions : List<String>):Boolean
    {   var isEqual:Boolean = false
        var sum:Int=0
        if (neededOptions.size!=0){


        for ( item in neededOptions)
        {
            if (carOptions.contains(item))
            {sum=sum+item.length}
        }

        if ( (sum+(neededOptions.size*2+2*(neededOptions.size-1))+4)==carOptions.length){isEqual=true}


    }
        return isEqual
    }



}
