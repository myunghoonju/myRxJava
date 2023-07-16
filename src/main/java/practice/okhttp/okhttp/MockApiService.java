package practice.okhttp.okhttp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

import java.util.Map;

public interface MockApiService {

    @POST("/myboot")
    Call<TestModel> mockPost(@HeaderMap Map<String, String> header, @Body TestModel testModel);
}
