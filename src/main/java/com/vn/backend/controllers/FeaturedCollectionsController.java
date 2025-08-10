package com.vn.backend.controllers;

import com.vn.backend.dto.response.FeaturedCollectionsResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.services.FeaturedCollectionsService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/featured-collections")
public class FeaturedCollectionsController {
    
    private static final Logger logger = LoggerFactory.getLogger(FeaturedCollectionsController.class);
    
    @Autowired
    private FeaturedCollectionsService featuredCollectionsService;
    
    @GetMapping
    public ResponseData<List<FeaturedCollectionsResponse>> getFeaturedCollections() {
        logger.info("[IN] GET /api/featured-collections");
        List<FeaturedCollectionsResponse> response = featuredCollectionsService.getFeaturedCollections();
        logger.info("[OUT] GET /api/featured-collections - Success");
        return ResponseData.success(response);
    }
}
