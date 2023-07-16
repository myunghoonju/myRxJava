package practice.okhttp.okhttp;

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

}
