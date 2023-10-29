package practice.okhttp.okhttp;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestModel {

    private String key;

    private String  val;
    @Builder
    public TestModel(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public static TestModel of(CompletableFuture<TestModel> m) {
        try {
            return TestModel.builder().key(m.get().key).val(m.get().val).build();
        } catch (Exception e) {

        }

        return TestModel.builder().build();
    }

}
