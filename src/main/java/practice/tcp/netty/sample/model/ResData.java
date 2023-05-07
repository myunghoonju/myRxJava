package practice.tcp.netty.sample.model;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ResData {

    private int anInt;

    @Builder
    public ResData(int anInt) {
        this.anInt = anInt;
    }
}
