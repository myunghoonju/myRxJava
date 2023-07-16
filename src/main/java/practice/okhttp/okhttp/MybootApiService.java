package practice.okhttp.okhttp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MybootApiService {

    @GET("test")
    Call<Object> push();

}
