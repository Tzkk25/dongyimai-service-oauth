package com.offcn.search.controller;

import com.offcn.entity.Result;
import com.offcn.entity.StatusCode;
import com.offcn.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuController {

    @Autowired
    SkuService skuService;

    /**
     * 导入数据库的数据到el中
     *
     * @return
     */
    @GetMapping("/import")
    public Result importSku() {
        try {
            skuService.importSku();
            return new Result(true, StatusCode.OK, "导入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "导入失败，" + e.getMessage());
        }
    }

    /**
     * 将el中导入的数据库信息删除掉
     *
     * @return
     */
    @DeleteMapping("/delete")
    public Result delete() {
        skuService.clearAll();
        return new Result(true, StatusCode.OK, "清空成功");
    }

    /**
     * 搜索
     *
     * @param searchMap
     * @return
     */
    @PostMapping
    public Map search(@RequestBody(required = false) Map searchMap) {
        return skuService.search(searchMap);
    }
}
