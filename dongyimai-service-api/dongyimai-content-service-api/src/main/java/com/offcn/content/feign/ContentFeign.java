package com.offcn.content.feign;

import com.offcn.content.pojo.Content;
import com.offcn.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/****
 * @Author:ujiuye
 * @Description:
 * @Date 2021/2/1 14:19
 *****/
@FeignClient(value="dym-content")
@RequestMapping("/content")
public interface ContentFeign {

    @GetMapping("/list/category/{id}") // request   path
    Result<List<Content>> findByCategory(@PathVariable("id") Long id) ;

}