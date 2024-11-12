package org.example.client;

import org.example.Dtos.ProductStockRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "product", url ="http://localhost:8080/")
public interface ProductClient {
    @RequestMapping(method = RequestMethod.POST, value = "api/v1/product/availability")
    boolean isInStock(@RequestBody ProductStockRequestDto productRequestDto);
}
