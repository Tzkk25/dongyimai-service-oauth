package com.offcn.itempage.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.itempage.service.PageService;
import com.offcn.entity.Result;
import com.offcn.sellergoods.entity.GoodsEntity;
import com.offcn.sellergoods.feign.GoodsFeign;
import com.offcn.sellergoods.feign.ItemCatFeign;
import com.offcn.sellergoods.pojo.Goods;
import com.offcn.sellergoods.pojo.GoodsDesc;
import com.offcn.sellergoods.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private ItemCatFeign itemCatFeign;

    @Autowired
    private TemplateEngine templateEngine;

    //生成静态文件路径
    @Value("${pagepath}")
    private String pagepath;

    /**
     * 构建数据模型
     *
     * @param spuId
     * @return
     */
    private Map<String, Object> buildDataModel(Long spuId) {
        //构建数据模型
        Map<String, Object> dataMap = new HashMap<>();
        //获取SPU 和SKU列表
        Result<GoodsEntity> result = goodsFeign.findById(spuId);
        GoodsEntity goodsEntity = result.getData();

        //1.加载SPU数据
        Goods goods = goodsEntity.getGoods();
        //2.加载商品扩展数据
        GoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        //3.加载SKU数据
        List<Item> itemList = goodsEntity.getItemList();

        dataMap.put("goods", goods);
        dataMap.put("goodsDesc", goodsDesc);
        dataMap.put("itemList", itemList);

        dataMap.put("specificationList", JSON.parseArray(goodsDesc.getSpecificationItems(), Map.class));
        dataMap.put("imageList", JSON.parseArray(goodsDesc.getItemImages(), Map.class));


        //4.加载分类数据
        dataMap.put("category1", itemCatFeign.findById((long) goods.getCategory1Id().intValue()).getData());
        dataMap.put("category2", itemCatFeign.findById((long) goods.getCategory2Id().intValue()).getData());
        dataMap.put("category3", itemCatFeign.findById((long) goods.getCategory3Id().intValue()).getData());

        return dataMap;
    }

    /***
     * 生成静态页
     * @param spuId
     */
    @Override
    public void createPageHtml(Long spuId) {
        // 1.上下文
        Context context = new Context();
        Map<String, Object> dataModel = buildDataModel(spuId);
        context.setVariables(dataModel);

        // 2.准备文件
        File dir = new File(pagepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(dir, spuId + ".html");

        // 3.生成页面
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(dest, "UTF-8");
            templateEngine.process("item", context, writer);// templates/item.html （模板）
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * 删除静态资源
     *
     * @param spuId
     */
    @Override
    public void deletePageHtml(Long spuId) {
        File file = new File(pagepath, spuId + ".html");
        file.delete();
//        new File(pagepath+"/" + spuId + ".html");
    }
}