package xdesign.georgi.espc_retrofit.Backend;

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

public interface ESPCService {

    @GET("UserRoomRatings")
    Call<List<UserRoomRating>> getAllUserRoomRatings();

    @GET("UserRoomRatings/{id}")
    Call<UserRoomRating> getUserRoomRatingById(@Path("id") int id);

    @POST("UserRoomRatings")
    Call<UserRoomRating> addNewUserRoomRating(@Body UserRoomRating userroomrating);

    @PUT("UserRoomRatings/{id}")
    Call<UserRoomRating> updateUserRoomRatingbyId(@Path("id") int id, @Body UserRoomRating userroomrating);

    @DELETE("UserRoomRatings/{id}")
    Call<HashMap<String, Integer>> deleteUserRoomRatingById(@Path("id") int id);

    @GET("UserPropertyRatings")
    Call<List<UserPropertyRating>> getAllUserPropertyRatings();

    @GET("UserPropertyRatings/{id}")
    Call<UserPropertyRating> getUserPropertyRatingById(@Path("id") int id);

    @POST("UserPropertyRatings")
    Call<UserPropertyRating> addNewUserPropertyRating(@Body UserPropertyRating userpropertyrating);

    @PUT("UserPropertyRatings/{id}")
    Call<UserPropertyRating> updateUserPropertyRatingbyId(@Path("id") int id, @Body UserPropertyRating userpropertyrating);

    @DELETE("UserPropertyRatings/{id}")
    Call<HashMap<String, Integer>> deleteUserPropertyRatingById(@Path("id") int id);

    @GET("User_ESPCs")
    Call<List<User_ESPC>> getAllUser_ESPCs();

    @GET("User_ESPCs/{id}")
    Call<User_ESPC> getUser_ESPCById(@Path("id") int id);

    @POST("User_ESPCs")
    Call<User_ESPC> addNewUser_ESPC(@Body User_ESPC user_espc);

    @PUT("User_ESPCs/{id}")
    Call<User_ESPC> updateUser_ESPCbyId(@Path("id") int id, @Body User_ESPC user_espc);

    @DELETE("User_ESPCs/{id}")
    Call<HashMap<String, Integer>> deleteUser_ESPCById(@Path("id") int id);

    @GET("Syncs")
    Call<List<Sync>> getAllSyncs();

    @GET("Syncs/{id}")
    Call<Sync> getSyncById(@Path("id") int id);

    @POST("Syncs")
    Call<Sync> addNewSync(@Body Sync sync);

    @PUT("Syncs/{id}")
    Call<Sync> updateSyncbyId(@Path("id") int id, @Body Sync sync);

    @DELETE("Syncs/{id}")
    Call<HashMap<String, Integer>> deleteSyncById(@Path("id") int id);

    @GET("Rooms")
    Call<List<Room>> getAllRooms();

    @GET("Rooms/{id}")
    Call<Room> getRoomById(@Path("id") int id);

    @POST("Rooms")
    Call<Room> addNewRoom(@Body Room room);

    @PUT("Rooms/{id}")
    Call<Room> updateRoombyId(@Path("id") int id, @Body Room room);

    @DELETE("Rooms/{id}")
    Call<HashMap<String, Integer>> deleteRoomById(@Path("id") int id);

    @GET("Propertys")
    Call<List<Property>> getAllPropertys();

    @GET("Propertys/{id}")
    Call<Property> getPropertyById(@Path("id") int id);

    @POST("Propertys")
    Call<Property> addNewProperty(@Body Property property);

    @PUT("Propertys/{id}")
    Call<Property> updatePropertybyId(@Path("id") int id, @Body Property property);

    @DELETE("Propertys/{id}")
    Call<HashMap<String, Integer>> deletePropertyById(@Path("id") int id);

    @GET("Features")
    Call<List<Feature>> getAllFeatures();

    @GET("Features/{id}")
    Call<Feature> getFeatureById(@Path("id") int id);

    @POST("Features")
    Call<Feature> addNewFeature(@Body Feature feature);

    @PUT("Features/{id}")
    Call<Feature> updateFeaturebyId(@Path("id") int id, @Body Feature feature);

    @DELETE("Features/{id}")
    Call<HashMap<String, Integer>> deleteFeatureById(@Path("id") int id);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
