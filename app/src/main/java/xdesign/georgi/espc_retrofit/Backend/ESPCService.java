package xdesign.georgi.espc_retrofit.Backend;

import android.content.Intent;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ESPCService {
    //=============================================== Feature Methods =============================================
    /***
     * Method to get a feature by id
     *
     * @param id id of the feature to be returned
     * @return a Feature object
     */
    @GET("Features/{id}")
    Call<Feature> getFeatureById(@Path("id") int id);

    /***
     * Method to get all of the features in the Feature table
     *
     * @return List of features
     */
    @GET ("Features/{}")
    Call<List<Feature>> getAllFeatures();

//=============================================== Property Methods =============================================
    @GET ("Properties")
    Call<List<Property>> getAllProperties();

    // Add new property
    @POST("Properties")
    Call<Property> addNewProperty(@Body Property property);

    @DELETE ("Properties/{id}")
    Call<HashMap<String, Integer>> deletePropertyById(@Path("id") int id);

    //=============================================== User Methods =============================================
    @GET ("User_ESPCs")
    Call<List<User_ESPC>> getAllUsers();

    //http://localhost:3000/api/User_ESPCs?filter[where][name]=admin
    @GET ("User_ESPCs")
    Call<List<User_ESPC>> getUserByNameAndPass(@Query("filter[where][name]=") String name, @Query("filter[where][password]=") String password);





    //=============================================== Backend Base URL =============================================
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}