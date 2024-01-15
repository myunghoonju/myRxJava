package practice.rxJava.sink;

import lombok.extern.slf4j.Slf4j;
import practice.rxJava.utility.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;

@Slf4j
public class SinksManyUnicast {

    public static void main(String[] args) {
        Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer(); // for only one subscriber
        Flux<Integer> fluxView = unicastSink.asFlux();

        unicastSink.emitNext(1, EmitFailureHandler.FAIL_FAST);
        unicastSink.emitNext(2, EmitFailureHandler.FAIL_FAST);

        fluxView.subscribe(data -> Log.onNext("Subscriber1", data));

        unicastSink.emitNext(3, EmitFailureHandler.FAIL_FAST);

        fluxView.subscribe(data -> Log.onNext("Subscriber2", data));
    }
}
