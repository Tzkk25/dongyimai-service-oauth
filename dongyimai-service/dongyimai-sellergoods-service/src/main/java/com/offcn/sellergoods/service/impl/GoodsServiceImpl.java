package com.offcn.sellergoods.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.dao.*;
import com.offcn.sellergoods.entity.GoodsEntity;
import com.offcn.sellergoods.pojo.Goods;
import com.offcn.sellergoods.pojo.GoodsDesc;
import com.offcn.sellergoods.pojo.Item;
import com.offcn.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Goods业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
//@Transactional
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements GoodsService {


    @Autowired
    GoodsDescMapper goodsDescMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemCatMapper itemCatMapper;

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    SellerMapper sellerMapper;

    @Autowired
    GoodsMapper goodsMapper;


    /**
     * Goods条件+分页查询
     * @param goods 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Goods> findPage(Goods goods, int page, int size){
        Page<Goods> mypage = new Page<>(page, size);
        QueryWrapper<Goods> queryWrapper = this.createQueryWrapper(goods);
        IPage<Goods> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Goods>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Goods分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Goods> findPage(int page, int size){
        Page<Goods> mypage = new Page<>(page, size);
        IPage<Goods> iPage = this.page(mypage, new QueryWrapper<Goods>());

        return new PageResult<Goods>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Goods条件查询
     * @param goods
     * @return
     */
    @Override
    public List<Goods> findList(Goods goods){
        //构建查询条件
        QueryWrapper<Goods> queryWrapper = this.createQueryWrapper(goods);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Goods构建查询对象
     * @param goods
     * @return
     */
    public QueryWrapper<Goods> createQueryWrapper(Goods goods){
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if(goods!=null){
            // 主键
            if(!StringUtils.isEmpty(goods.getId())){
                queryWrapper.eq("id",goods.getId());
            }
            // 商家ID
            if(!StringUtils.isEmpty(goods.getSellerId())){
                queryWrapper.eq("seller_id",goods.getSellerId());
            }
            // SPU名
            if(!StringUtils.isEmpty(goods.getGoodsName())){
                queryWrapper.eq("goods_name",goods.getGoodsName());
            }
            // 默认SKU
            if(!StringUtils.isEmpty(goods.getDefaultItemId())){
                queryWrapper.eq("default_item_id",goods.getDefaultItemId());
            }
            // 状态
            if(!StringUtils.isEmpty(goods.getAuditStatus())){
                queryWrapper.eq("audit_status",goods.getAuditStatus());
            }
            // 是否上架
            if(!StringUtils.isEmpty(goods.getIsMarketable())){
                queryWrapper.eq("is_marketable",goods.getIsMarketable());
            }
            // 品牌
            if(!StringUtils.isEmpty(goods.getBrandId())){
                queryWrapper.eq("brand_id",goods.getBrandId());
            }
            // 副标题
            if(!StringUtils.isEmpty(goods.getCaption())){
                queryWrapper.eq("caption",goods.getCaption());
            }
            // 一级类目
            if(!StringUtils.isEmpty(goods.getCategory1Id())){
                queryWrapper.eq("category1_id",goods.getCategory1Id());
            }
            // 二级类目
            if(!StringUtils.isEmpty(goods.getCategory2Id())){
                queryWrapper.eq("category2_id",goods.getCategory2Id());
            }
            // 三级类目
            if(!StringUtils.isEmpty(goods.getCategory3Id())){
                queryWrapper.eq("category3_id",goods.getCategory3Id());
            }
            // 小图
            if(!StringUtils.isEmpty(goods.getSmallPic())){
                queryWrapper.eq("small_pic",goods.getSmallPic());
            }
            // 商城价
            if(!StringUtils.isEmpty(goods.getPrice())){
                queryWrapper.eq("price",goods.getPrice());
            }
            // 分类模板ID
            if(!StringUtils.isEmpty(goods.getTypeTemplateId())){
                queryWrapper.eq("type_template_id",goods.getTypeTemplateId());
            }
            // 是否启用规格
            if(!StringUtils.isEmpty(goods.getIsEnableSpec())){
                queryWrapper.eq("is_enable_spec",goods.getIsEnableSpec());
            }
            // 是否删除
//            if(!StringUtils.isEmpty(goods.getIsDelete())){
//                 queryWrapper.eq("is_delete",goods.getIsDelete());
//            }

            //条件过滤以删除
            queryWrapper.eq("is_delete","0");
        }
        return queryWrapper;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
//        this.removeById(id);//delete 语句


        Goods goods = goodsMapper.selectById(id);

        if(goods==null){
            throw new RuntimeException("商品不存在");
        }

        if(goods.getIsMarketable().equals("1")){
            throw new RuntimeException("请先下架，在进行删除");
        }

        goods.setIsDelete("1");
        goods.setAuditStatus("0");//删除的商品，审核状态统一设置成0（未审核）

        goodsMapper.updateById(goods);

    }

    /**
     * 修改Goods
     * @param goods
     */
    @Override
    public void update(Goods goods){
        this.updateById(goods);
    }

    /**
     * 增加Goods
     * @param goodsEntity
     */
    @Transactional
    @Override
    public void add(GoodsEntity goodsEntity){
//        this.save(goods);

        // 1、添加spu
        Goods goods = goodsEntity.getGoods();
        goods.setAuditStatus("0");//商品审核状态，0 未审核  1 通过  2 驳回
        goods.setIsMarketable("0");// 商品上下架状态  0 未上架  1 已上架
        goods.setIsDelete("0");//是否删除  0 未删除  1已删除

        this.save(goods);

//        int i = 1/0;

        // 2、添加goodsDesc（goodsDesc和goods使用同一个主键id）
        goodsEntity.getGoodsDesc().setGoodsId(goods.getId());
        goodsDescMapper.insert(goodsEntity.getGoodsDesc());

        // 3、添加sku列表

        // 如果没有启用规格，是没有sku列表

        if(goods.getIsEnableSpec().equals("1")){
            List<Item> itemList = goodsEntity.getItemList();

            if(itemList!=null && itemList.size()>0){
                for (Item item : itemList) {
                    // id
                    // title = spu名称+各个规格选项值  苹果13 黑色 128G 6.8寸

                    item.setTitle(this.getTitle(goods,item));//sku商品标题
                    item.setSellPoint("性价比高,耐用性强");
                    item.setImage(this.getDefaultImageUrl(goodsEntity));// 当前sku的商品图片，可以单独为每一个sku录入不同的图片，但是，讲义中默认将goodsDesc.item_image中第一个图片取出赋值给它
                    item.setCategoryId(goodsEntity.getGoods().getCategory3Id());//第三级分类id
                    item.setCreateTime(new Date());
                    item.setUpdateTime(new Date());
                    item.setGoodsId(goods.getId());//记录当前sku属于哪个spu
                    item.setSellerId(goods.getSellerId());
                    item.setCartThumbnail(this.getDefaultImageUrl(goodsEntity));//购物车图片
                    item.setCategory(itemCatMapper.selectById(goods.getCategory3Id()).getName());//第三级分类名称
                    item.setBrand(brandMapper.selectById(goods.getBrandId()).getName());//品牌的名称
                    item.setSeller(sellerMapper.selectById(goods.getSellerId()).getName());//商家名称

                    itemMapper.insert(item);
                }
            }
        }else{
            //未启用规格，创建一个默认的item对象即可
            Item item = new Item();
            item.setGoodsId(goods.getId());
            item.setTitle(goods.getGoodsName());
            item.setPrice(goods.getPrice());
            item.setNum(9999);
            itemMapper.insert(item);
        }



    }

    private String getDefaultImageUrl(GoodsEntity goodsEntity){
        String itemImages = goodsEntity.getGoodsDesc().getItemImages();// [{color,url},{}]

        List<Map> maps = JSON.parseArray(itemImages, Map.class);
        if(maps!=null && maps.size()>0){
            return String.valueOf(maps.get(0).get("url"));
        }
        return "";
    }

    private String getTitle(Goods goods,Item item){
        String goodsName = goods.getGoodsName()+" ";
        String spec = item.getSpec();//{"机身内存":"16G","网络":"移动4G"}
        Map map = JSON.parseObject(spec, Map.class);
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            goodsName += value;
        }
        return goodsName;
    }

    /**
     * 根据ID查询Goods
     * @param id
     * @return
     */
    @Override
    public GoodsEntity findById(Long id){
        Goods goods = this.getById(id);

        GoodsDesc goodsDesc = goodsDescMapper.selectById(id);


        // select * from tb_item where goods_id = ?

        QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("goods_id",id);

        List<Item> items = itemMapper.selectList(itemQueryWrapper);

        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoods(goods);
        goodsEntity.setGoodsDesc(goodsDesc);
        goodsEntity.setItemList(items);

        return goodsEntity;

    }

    /**
     * 查询Goods全部数据
     * @return
     */
    @Override
    public List<Goods> findAll() {
        return this.list(new QueryWrapper<Goods>());
    }

    @Override
    public void audit(Long id) {
        Goods goods = this.getById(id);
        if(goods==null){
            throw new RuntimeException("商品不存在");
        }

        //判断商品是否是被删除的
        if(goods.getIsDelete().equals("1")){
            throw new RuntimeException("该商品已被删除");
        }


        goods.setAuditStatus("1");
        goods.setIsMarketable("1");//审核通过，自动上架

        this.updateById(goods);

    }

    @Override
    public void pull(Long goodsId) {
        Goods goods = this.getById(goodsId);
        if(goods==null){
            throw new RuntimeException("商品不存在");
        }

        //判断商品是否是被删除的
        if(goods.getIsDelete().equals("1")){
            throw new RuntimeException("该商品已被删除");
        }

        if(!goods.getAuditStatus().equals("1")){
            throw new RuntimeException("该商品未审核通过，不能进行上下架");
        }

        if(goods.getIsMarketable().equals("0")){
            throw new RuntimeException("该商品已经处于下架状态");
        }

        goods.setIsMarketable("0");//下架

        this.updateById(goods);

    }

    @Override
    public void put(Long goodsId) {
        Goods goods = this.getById(goodsId);
        if(goods==null){
            throw new RuntimeException("商品不存在");
        }

        //判断商品是否是被删除的
        if(goods.getIsDelete().equals("1")){
            throw new RuntimeException("该商品已被删除");
        }

        if(!goods.getAuditStatus().equals("1")){
            throw new RuntimeException("该商品未审核通过，不能进行上下架");
        }

        if(goods.getIsMarketable().equals("1")){
            throw new RuntimeException("该商品已经处于上架状态");
        }

        goods.setIsMarketable("1");//上架

        this.updateById(goods);
    }

    @Override
    public int putMany(Long[] ids) {

//        UPDATE `tb_goods` SET is_marketable = '1' WHERE id IN (149187842867990,149187842867991) AND is_delete=0 AND audit_status=1 AND is_marketable=0


        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.in("id",Arrays.asList(ids));
        goodsQueryWrapper.eq("is_delete","0");
        goodsQueryWrapper.eq("audit_status","1");
        goodsQueryWrapper.eq("is_marketable","0");


        Goods goods = new Goods();
        goods.setIsMarketable("1");

        int count = goodsMapper.update(goods, goodsQueryWrapper);

        return count;
    }
}
