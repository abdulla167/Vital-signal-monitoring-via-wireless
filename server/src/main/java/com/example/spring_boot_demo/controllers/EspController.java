package com.example.spring_boot_demo.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@RestController
public class EspController {
    private List<SseEmitter> emitters = new ArrayList<>();

    @GetMapping("/")
    public SseEmitter index() {
        SseEmitter sseEmitter = new SseEmitter(-1L);
        emitters.add(sseEmitter);
        sseEmitter.onCompletion(() -> {
            emitters.remove(sseEmitter);
        });
        return sseEmitter;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/push")
    public void insert(
         @RequestBody String data
    ) {
        System.out.println(data);
        for(SseEmitter emitter : emitters){
            try {
                emitter.send(SseEmitter.event().data(data));
            } catch (IOException e){
            }
        }
    }
}