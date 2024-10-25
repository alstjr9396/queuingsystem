package me.minseok.webflux1.service;

import lombok.RequiredArgsConstructor;
import me.minseok.webflux1.repository.Post;
import me.minseok.webflux1.repository.PostR2dbcRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostServiceV2 {

    private final PostR2dbcRepository postR2dbcRepository;

    public Mono<Post> create(Long memberId, String title, String content) {
        return postR2dbcRepository.save(Post.builder()
                .memberId(memberId)
                .title(title)
                .content(content)
                .build());
    }

    public Flux<Post> findAll() {
        return postR2dbcRepository.findAll();
    }

    public Mono<Post> findById(Long id) {
        return postR2dbcRepository.findById(id);
    }

    public Mono<Void> deleteById(Long id) {
        return postR2dbcRepository.deleteById(id);
    }
}
