package practice.rxJava.sink;

import lombok.extern.slf4j.Slf4j;
import practice.rxJava.utility.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;

@Slf4j
public class SinksManyReplayOne {

    public static void main(String[] args) {
        Many<Integer> multicastSink = Sinks.many().replay().limit(2);
        Flux<Integer> fluxView = multicastSink.asFlux();

        multicastSink.emitNext(1, EmitFailureHandler.FAIL_FAST);
        multicastSink.emitNext(2, EmitFailureHandler.FAIL_FAST);
        multicastSink.emitNext(3, EmitFailureHandler.FAIL_FAST);

        fluxView.subscribe(data -> Log.onNext("Subscriber1", data));
        fluxView.subscribe(data -> Log.onNext("Subscriber2", data));
    }
}
