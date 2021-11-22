package com.offcn.sellergoods.feign;

import com.offcn.entity.Result;
import com.offcn.sellergoods.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name="dym-sellergoods")
@RequestMapping("/item")
public interface ItemFeign {
    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Item>> findByStatus(@PathVariable("status") String status);

    /**
     * 根据skuid查询sku商品对象
     * @param id
     * @return
     */
    @GetMapping("/{id}")

    public Result<Item> findById(@PathVariable("id") Long id);
}
