## Reactor

### Flux
    
    0-N개 아이템을 가질 수 있는 데이터 스트림
    onNext(N), onComplete, onError

### Mono

    0개 또는 1개의 아이템을 가지는 데이터 스트림
    onNext(0-1), onComplete, onError

### StepVerifier

    Reactor 자동화 테스트 도구

### Operator

    * map
    * filter
    * take
    * flatMap(비동기)
    * concatMap(동기)
    * flatMapMany
    * switchIfEmpty / defaultIfEmpty
    * merge / zip
    * count
    * distinct
    * reduce
    * groupby
    * delaySequence / limitRate
    * sample

### Schedulers

    publishOn(scheduler) / subscribeOn(scheduler)
    Schedulers.immediate(): 현재 스레드에서 동기적인 작업
    Schedulers.simgle(): 단일 백그라운드 스레드에서 작업
    Schedulers.parallel(): 병렬 스레드 풀
    Schedulers.boundedElastic(): 유연한 스레드 풀


