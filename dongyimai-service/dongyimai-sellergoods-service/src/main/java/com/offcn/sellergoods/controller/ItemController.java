package com.offcn.sellergoods.controller;


import com.offcn.entity.Result;
import com.offcn.entity.StatusCode;
import com.offcn.sellergoods.pojo.Item;
import com.offcn.sellergoods.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService;


    @GetMapping("/status/{status}")
    public Result<List<Item>> findByStatus(@PathVariable("status") String status){
        List<Item> list = itemService.findByStatus(status);
        return new Result<>(true,StatusCode.OK,"查询成功",list);
    }

    @GetMapping("/{id}")
    public Result<Item> findById(@PathVariable("id") Long id){
        return new Result<>(true,StatusCode.OK,"查询成功", itemService.findById(id));
    }


}
