package com.offcn.sellergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.sellergoods.dao.ItemMapper;
import com.offcn.sellergoods.pojo.Item;
import com.offcn.sellergoods.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper,Item> implements ItemService {


    @Override
    public List<Item> findByStatus(String status) {

        QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("status",status);

        return this.list(itemQueryWrapper);
    }

    @Override
    public Item findById(Long id) {
        return this.getById(id);
    }
}
