package xdesign.georgi.espc_retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xdesign.georgi.espc_retrofit.Utils.Property;
import xdesign.georgi.espc_retrofit.Utils.User_ESPC;

interface ESPCService {
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


    @GET ("Properties")
    Call<List<Property>> getAllProperties();

    @GET ("User_ESPCs")
    Call<List<User_ESPC>> getAllUsers();




    //http://localhost:3000/api/User_ESPCs?filter[where][name]=admin

    @GET ("User_ESPCs")
    Call<List<User_ESPC>> getUserByNameAndPass(@Query("filter[where][name]=") String name, @Query("filter[where][password]=") String password);






    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}