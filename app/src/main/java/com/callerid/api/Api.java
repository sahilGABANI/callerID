package com.callerid.api;

import com.callerid.model.ActivityResponseModel;
import com.callerid.model.ArrayResponseModel;
import com.callerid.model.ContentResponseModel;
import com.callerid.model.IgnoreListResponse;
import com.callerid.model.LinkRequestModel;
import com.callerid.model.NoteModel;
import com.callerid.model.NoteResponseModel;
import com.callerid.model.ObjectResponseModel;
import com.callerid.model.PhoneModel;
import com.callerid.model.RequestModel;
import com.callerid.model.ResponseModel;
import com.callerid.model.SimpleResponseModel;
import com.callerid.model.TaskResponseModel;
import com.callerid.model.UploadImage;

import java.util.ArrayList;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface Api {
//    @POST("getUserByPhone")

    @FormUrlEncoded
    @POST("lead/by-phone-or-email")
    Single<ResponseModel> getUserByPhone(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("note")
    Single<SimpleResponseModel> CreateNote(@Field("lead") String lead, @Field("description") String description);

    @GET("note")
    Single<NoteResponseModel> GetNotes(@Query("orderBy") String orderBy, @Query("isAscending") String isAscending,
                                       @Query("page") String page, @Query("perPage") String perPage, @Query("leadId") String leadId);

    @FormUrlEncoded
    @POST("task")
    Single<SimpleResponseModel> CreateTask(@Field("leadIds[]") ArrayList<String> leadIds, @Field("type") String type,
                                           @Field("toBePerformAt") String toBePerformAt);

    @GET("task")
    Single<TaskResponseModel> GetTasks(@Query("orderBy") String orderBy, @Query("isAscending") String isAscending,
                                       @Query("page") String page, @Query("perPage") String perPage, @Query("leadId") String leadId);

    @GET("content")
    Single<ContentResponseModel> GetContent(@Query("orderBy") String orderBy, @Query("isAscending") String isAscending,
                                            @Query("page") String page, @Query("perPage") String perPage);

    @GET("content")
    Single<ContentResponseModel> GetContentSpacific(@Query("orderBy") String orderBy, @Query("isAscending") String isAscending,
                                            @Query("page") String page, @Query("perPage") String perPage,
                                                    @Query("type") String type);

    @GET("activity")
    Single<ActivityResponseModel> GetActivity(@Query("orderBy") String orderBy, @Query("isAscending") String isAscending,
                                              @Query("page") String page, @Query("perPage") String perPage,
                                              @Query("byOrganization") String byOrganization, @Query("leadId") String leadId);

    @POST("generate_unique_link")
    Single<ObjectResponseModel> GetLink(@Body LinkRequestModel requestModel);


    @FormUrlEncoded
    @PUT("lead/status")
    Single<SimpleResponseModel> updateStatus(@Field("leadIDs[]") ArrayList<String> leadIds, @Field("status") String status);


    @FormUrlEncoded
    @PUT("lead/label")
    Single<SimpleResponseModel> updateLabel(@Field("leadIDs[]") ArrayList<String> leadIds,@Field("label[]") ArrayList<String> label);

    @FormUrlEncoded
    @POST("utility/dialog-box-ignore-list")
    Single<SimpleResponseModel> addDontShow(@Field("phone") String phone);

    @GET("utility/dialog-box-ignore-list")
    Single<IgnoreListResponse> getDontShowList();


    @Multipart
    @POST("activity")
    Single<UploadImage> addAttachment(@Part("leadIds[]") RequestBody leadIds,
                                      @Part("type") RequestBody type,
                                      @Part("performedAt") RequestBody performedAt,
                                      @Part MultipartBody.Part file);

   /* @POST("create_note_with_token")
    Single<SimpleResponseModel> CreateNote(@Body NoteModel noteModel);


    @POST("get_notes_with_token")
    Single<NoteResponseModel> GetNotes(@Body RequestModel requestModel);


    @POST("create_task_with_token")
    Single<SimpleResponseModel> CreateTask(@Body RequestModel requestModel);


    @POST("get_tasks_with_token/0/100")
    Single<TaskResponseModel> GetTasks(@Body RequestModel requestModel);


    @POST("get_content_version_2_with_token")
    Single<ContentResponseModel> GetContent(@Body RequestModel requestModel);


    @POST("get_activities_with_token")
    Single<ActivityResponseModel> GetActivity(@Body RequestModel requestModel);


    @POST("generate_unique_link")
    Single<ObjectResponseModel> GetLink(@Body LinkRequestModel requestModel);*/
}