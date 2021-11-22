package com.offcn.sellergoods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.offcn.sellergoods.pojo.Item;

import java.util.List;

public interface ItemService extends IService<Item> {

    /**
     * 根据商品状态查询sku列表
     * @param status
     * @return
     */
    List<Item> findByStatus(String status);

    Item findById( Long id);

}
