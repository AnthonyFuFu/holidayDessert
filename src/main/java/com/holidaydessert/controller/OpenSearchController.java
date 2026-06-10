package com.holidaydessert.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.SearchConditionDto;
import com.holidaydessert.service.OpenSearchService;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "api/openSearch")
@Tag(name = "api/openSearch")
@Timed(value = "greeting.time", description = "Time taken to return greeting")
public class OpenSearchController {

    @Autowired
    OpenSearchService openSearchService;

    @Operation(summary = "advance search")
    @PostMapping("/openSearch")
    public ResponseEntity<?> memberSearch(@RequestBody List<SearchConditionDto> list) throws Exception {

		ApiReturnObject apiReturnObject = openSearchService.OpenSearch(list);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);

    }
}
