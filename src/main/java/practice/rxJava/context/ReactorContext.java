package practice.rxJava.context;

import lombok.extern.slf4j.Slf4j;
import practice.rxJava.utility.Log;
import practice.rxJava.utility.Time;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ReactorContext {

    public static void main(String[] args) {
        String key = "msg";
        Mono<String> mono = Mono.deferContextual(ctx -> Mono.just("Hello " + ctx.get(key)).doOnNext(Log::doOnNext))
                                .subscribeOn(Schedulers.boundedElastic())
                                .publishOn(Schedulers.parallel())
                                .transformDeferredContextual((mono2, ctx) -> mono2.map(data -> data + " " + ctx.get(key)))
                                .contextWrite(context -> context.put(key, "Reactor"));

        mono.subscribe(Log::doOnNext);
        Time.sleep(100L);
    }
}
