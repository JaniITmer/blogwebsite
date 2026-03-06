package com.JaniITmer.webapp.controller;

import com.JaniITmer.webapp.entity.Post;
import com.JaniITmer.webapp.entity.User;
import com.JaniITmer.webapp.repository.PostRepository;
import com.JaniITmer.webapp.repository.UserRepository;
import com.JaniITmer.webapp.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private static final String UPLOAD_DIR = "uploads/images/";

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestPart("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader("Authorization") String authHeader) {


        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);

        if (image != null && !image.isEmpty()) {
            try {

                Files.createDirectories(Paths.get(UPLOAD_DIR));

                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, image.getBytes());

                post.setImageUrl("/" + UPLOAD_DIR + fileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }

        Post saved = postRepository.save(post);
        return ResponseEntity.ok(saved);
    }
}