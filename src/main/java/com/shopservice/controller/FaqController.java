package com.shopservice.controller;

import com.shopservice.entity.Faq;
import com.shopservice.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    public List<Faq> getAll() {
        return faqService.getAll();
    }
}
