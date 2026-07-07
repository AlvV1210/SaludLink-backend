package com.saludlink.review.service;

import com.saludlink.review.dto.CreateReviewRequest;
import com.saludlink.review.dto.ReviewResponse;
import java.util.List;

public interface IReviewService {

    ReviewResponse create(Long userId, CreateReviewRequest request);

    List<ReviewResponse> listByDoctor(Long doctorId);
}
