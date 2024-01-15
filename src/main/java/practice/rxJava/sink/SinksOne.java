package practice.rxJava.sink;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

import lombok.extern.slf4j.Slf4j;
import practice.rxJava.utility.Log;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.One;

@Slf4j
public class SinksOne {
    // emit 된 데이터 중에서 단 하나의 데이터만 Subscriber에게 전달한다. 나머지 데이터는 Drop 됨.
    public static void main(String[] args) {
        One<String> sinkOne = Sinks.one();
        Mono<String> mono = sinkOne.asMono();

        sinkOne.emitValue("Hell Reactor", FAIL_FAST);

        mono.subscribe(d -> Log.onNext("sub 1", d));
        mono.subscribe(d -> Log.onNext("sub 2", d));
    }
}
