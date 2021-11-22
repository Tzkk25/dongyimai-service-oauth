package com.offcn.item.feign;

import com.offcn.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "dym-itempage")
@RequestMapping("/page")
public interface ItemPageFeign {

    /***
     * 根据SpuID生成静态页
     * @param
     * @return
     */
    @GetMapping("/createPageHtml/{spuId}")
    public Result createPageHtml(@PathVariable("spuId") Long spuId);

    @GetMapping("/deletePageHtml/{spuId}")
    public Result deletePageHtml(@PathVariable("spuId") Long spuId) ;}
