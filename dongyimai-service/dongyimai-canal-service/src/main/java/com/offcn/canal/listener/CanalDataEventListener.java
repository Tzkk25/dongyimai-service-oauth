package com.offcn.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.offcn.content.feign.ContentFeign;
import com.offcn.content.pojo.Content;
import com.offcn.entity.Result;
import com.offcn.item.feign.ItemPageFeign;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@CanalEventListener
public class CanalDataEventListener {

    @Autowired
    ContentFeign contentFeign;

    @Autowired
    ItemPageFeign itemPageFeign;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


//    @InsertListenPoint
//    public void test1(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
//        System.out.println("监听insert事件");
//    }
//
//    @UpdateListenPoint
//    public void test2(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
//        System.out.println("监听update事件");
//    }
//
//    @DeleteListenPoint
//    public void test3(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
//        System.out.println("监听delete事件");
//    }

    @ListenPoint(
            destination = "example",//xxx
            schema = "dongyimaidb",//数据库名称
            table = {"tb_content", "tb_content_category"},// 具体哪些表
            eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
// update事件
    public void test4(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        // insert -- row
        // delete -- row
        // update -- row

//        eventType 事件的类型

//        System.out.println("事件类型：" + eventType);
//        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
//        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
//        System.out.println(beforeColumnsList);// 如果是添加数据，没有before
//        System.out.println(afterColumnsList);// 如果是删除数据，没有after
        // update ，既有before 也有after

        // 调用广告服务，重新查询对应的广告列表，然后转成json字符串，存入redis中
        // content_xxx   ---   [{},{},{}..]

        // 获取有变化的广告所属分类id

        Long category_id = getCategoryId(eventType, rowData);

        Result<List<Content>> result = contentFeign.findByCategory(category_id);//远程调用广告服务
        List<Content> list = result.getData();

        if (list != null && list.size() > 0) {
            String jsonString = JSON.toJSONString(list);
            // content_1  jsonString --> redis
            stringRedisTemplate
                    .boundValueOps("content_" + category_id)
                    .set(jsonString, 10, TimeUnit.MINUTES);
        }
        System.out.println("数据更新成功");
    }

    private Long getCategoryId(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        Long category_id = null;
        List<CanalEntry.Column> columnsList = null;

        if (eventType == CanalEntry.EventType.DELETE) {
            columnsList = rowData.getBeforeColumnsList();
        } else {
            columnsList = rowData.getAfterColumnsList();
        }

        for (CanalEntry.Column column : columnsList) {
            if (column.getName().equals("category_id")) {
                category_id = Long.parseLong(column.getValue());
            }
        }
        return category_id;
    }

    //spu商品表数据有变化就会重新创建详情页面
    @ListenPoint(
            destination = "example",//xxx
            schema = "dongyimaidb",//数据库名称
            table = {"tb_goods"},// 具体哪些表
            eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
// update事件
    public void listenerGoods(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        Long id = Long.parseLong(this.getColumnValue(eventType, rowData, "id") + "");
        if (eventType == CanalEntry.EventType.DELETE) {
            //删除页面
            itemPageFeign.deletePageHtml(id);
        } else {
            //创建页面
            itemPageFeign.createPageHtml(id);
        }

    }


    private Object getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData, String columnName) {

        Object columnValue = null;
        List<CanalEntry.Column> columnsList = null;

        if (eventType == CanalEntry.EventType.DELETE) {
            columnsList = rowData.getBeforeColumnsList();
        } else {
            columnsList = rowData.getAfterColumnsList();
        }

        for (CanalEntry.Column column : columnsList) {
            if (column.getName().equals(columnName)) {
                columnValue = column.getValue();
            }
        }
        return columnValue;
    }
}
