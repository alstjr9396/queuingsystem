package me.minseok.webflux1.controller;

import lombok.RequiredArgsConstructor;
import me.minseok.webflux1.dto.PostCreateDto;
import me.minseok.webflux1.dto.PostResponseV2;
import me.minseok.webflux1.service.PostServiceV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v2/posts")
@RequiredArgsConstructor
public class PostControllerV2 {

    private final PostServiceV2 postServiceV2;

    @PostMapping("")
    public Mono<PostResponseV2> createPost(@RequestBody PostCreateDto postCreateDto) {
        return postServiceV2.create(postCreateDto.getMemberId(), postCreateDto.getTitle(), postCreateDto.getContent())
                .map(PostResponseV2::of);
    }

    @GetMapping("")
    public Flux<PostResponseV2> findAllPosts() {
        return postServiceV2.findAll()
                .map(PostResponseV2::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostResponseV2>> findById(@PathVariable Long id) {
        return postServiceV2.findById(id)
                .map(p -> ResponseEntity.ok().body(PostResponseV2.of(p)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<PostResponseV2>> deleteById(@PathVariable Long id) {
        return postServiceV2.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
