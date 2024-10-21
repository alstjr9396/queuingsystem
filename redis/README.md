# Queueing System Studying

## Redis
### run redis
    
    * docker pull redis:6.2
    * docker run --rm -it -d -p 6379:6379 redis:6.2
    * docker exec -it {{container_id}} redis-cli
    * monitor
    * slowlog get
    * info
    * docker exec -it {{container_id}} /bin/bash > redis-benchmark
    * cat redis-strings.txt | redis-cli --pipe
    * SCAN {{cursor}} MATCH * COUNT 100: CURSOR부터 100개 키를 조회


### command

    + String
        * SET: SET users:1:email test@email.com > OK
        * SET NX: 이미 설정된 값이 있으면 저장하지 않음
        * GET: GET users:1:email > "test@gmail.com"
        * MGET: MGET users:1:email name > 1) "test@gmail.com" 2) "100"
        * INCR counter: 1씩 증가
        * INCRBY counter 10: 10씩 증가
    
    + Key
        * EXPIRE {{key}} 10: 10초뒤 만료
        * TTL {{key}}: TTL 시간 확인
        * DEL {{key}}: 동기적으로 삭제
        * UNLINK {{key}}: 비동기적으로 삭제
        * MEMORY USAGE {{key}}: 메모리 사용량 확인

    + List
        * LPUSH: LPUSH books:favorites '{id:100}' > (integer) 1
        * RPUSH: RPUSH books:favorites '{id:200}' > (integer) 1
        * LPOP: LPOP books:favorites 1 > "{id:100}"
        * RPOP: RPOP books:favorites 1 > "{id:200}"
        * BLPOP {{time}} {{list}} : 리스트 데이터가 있으면 RPOP, 없으면 시간 동안 대기
        * LLEN: (integer) 2
        * LTRIM: 리스트 추출
        * LRANGE: LRANGE books:favorites 0 > "{id:100}"
            LANGE books:favorites 0 -1 전체 리스트 조회
 
    + Set
        * SADD: 추가
        * SREM: 삭제
        * SISMEMBER: 멤버인지확인 
        * SMEMBERS:  전체 데이터 조회
        * SINTER: 2개 이상의 Set에서 공통 데이터 조회
        * SCARD: Set에 포함된 데이터 개수 조회

    + Sorted set
        * ZADD: 추가
        * ZREM: 삭제
        * ZRANGE: Set 특정 범위 데이터 조회
        * ZCARD: Set에 포함된 데이터 개수 조회
        * ZRANK/ZREVRANK: 오름차순/내림차순 순위 조회
        * ZINCRBY

    + Hash
        * HSET: 추가
        * HGET, HMGET: 조회
        * HGETALL: 전체조회
        * HDEL: 삭제
        * HINCRBY

    + Geospatial
        * GEOADD: 저장 
        * GEOSEARCH: 위치 정보 조회
        * GEODIST: 위치간 거리
        * GEOPOS: 키로 위도/경도 조회

    + Bitmap
        * SETBIT
        * GETBIT
        * BITCOUNT

    + Transaction
        * MULTI: 트랜잭션 시작
        * EXEC: 트랜잭션 명령들의 집합 실행 
        * DISCARD: 트랜잭션 종료
        * WATCH: 동시에 같은 키를 수정하는 상황에서 트랜잭션 취소

    + PUB/SUB
        * SUBSCRIBE users:unregister
        * PUBSUB CHANNELS
        * PUBLISH users:unregister 100

