package com.example.taskservice.client;

import com.example.taskservice.dto.UserResponse;
import com.example.taskservice.fallback.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(
    name = "user-service",
    fallback = UserClientFallback.class
)
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users/{id}/exists")
    Map<String, Boolean> checkUserExists(@PathVariable("id") Long id);
}
