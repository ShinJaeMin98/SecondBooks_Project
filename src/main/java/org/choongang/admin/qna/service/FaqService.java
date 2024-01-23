package org.choongang.admin.qna.service;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.qna.repository.FaqRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaqService {

    private FaqRepository faqRepository;



}
