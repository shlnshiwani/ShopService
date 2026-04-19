package com.shopservice.service;

import com.shopservice.entity.Faq;
import com.shopservice.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepo;

    public List<Faq> getAll() {
        return faqRepo.findAll();
    }
}
