package practice.tcp.netty.sample.model;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ReqData {

    private int anInt;
    private String string;

    @Builder
    public ReqData(int anInt, String string) {
        this.anInt = anInt;
        this.string = string;
    }
}
