package com.holidaydessert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.ProductCollectionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/collection")
@Api(tags = "商品分類")
public class CollectionController {

	@Autowired
	private ProductCollectionService productCollectionService;
	
	@PostMapping(value = "/getAllPdcList")
	@ApiOperation(value = "全部商品分類", notes = "列出全部商品分類")
	public ResponseEntity<?> getAllPdcList() {
		
		ApiReturnObject apiReturnObject = productCollectionService.getAllPdcList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/{pdcName}")
	@ApiOperation(value = "商品分類", notes = "在商品分類路徑下查詢商品")
	public ResponseEntity<?> getPdByPdcName(@PathVariable String pdcName) {
		
		ApiReturnObject apiReturnObject = productCollectionService.getPdByPdcName(pdcName);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
}
