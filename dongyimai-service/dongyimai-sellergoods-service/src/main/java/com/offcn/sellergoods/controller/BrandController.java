package com.offcn.sellergoods.controller;

import com.offcn.entity.PageResult;
import com.offcn.entity.Result;
import com.offcn.entity.StatusCode;
import com.offcn.sellergoods.pojo.Brand;
import com.offcn.sellergoods.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "BrandController")
@CrossOrigin
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    @ApiOperation(value = "查询所有", notes = "无条件无分页的查询所有品牌", tags = "BrandController")
    @GetMapping
    public Result<List<Brand>> findAll(HttpServletRequest request) {
        //获取请求头中的数据AUTHORIZE_TOKEN(clamis)
        String claims = request.getHeader("Authorization");
        System.out.println("令牌信息:"+claims);

        List<Brand> brandList = brandService.findAll();
        Result<List<Brand>> result = new Result<>(true, StatusCode.OK, "查询成功", brandList);
        return result;
    }

    @ApiOperation(value = "根据id查询", notes = "根据主键id查询一个品牌对象", tags = "BrandController")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "主键", paramType = "path", dataType = "Long")
//                    @ApiImplicitParam(name = "age",value = "年龄",paramType = "request")
            }
    )
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable("id") Long id) {
//    public Result<Brand> findById(@PathVariable("id") Long id,@RequestParam("age") int age){
        try {
            Brand brand = brandService.findById(id);
            return new Result<>(true, StatusCode.OK, "根据id查询成功", brand);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(false, StatusCode.ERROR, "根据id查询失败");
        }
    }

    @PostMapping
    public Result add(@RequestBody(required = true) Brand brand) {
        try {
            brandService.add(brand);
            return new Result(true, StatusCode.OK, "添加品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "添加品牌失败，" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Long id, @RequestBody Brand brand) {
        try {
            brand.setId(id);
            brandService.update(brand);
            return new Result(true, StatusCode.OK, "修改品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "修改品牌失败，" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id) {
        try {
            brandService.delete(id);
            return new Result(true, StatusCode.OK, "根据id删除品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "根据id删除品牌失败，" + e.getMessage());
        }
    }

    @PostMapping("/search")
    public Result<List<Brand>> findList(@RequestBody(required = false) Brand brand) {
        List<Brand> list = brandService.findList(brand);
        return new Result<>(true, StatusCode.OK, "条件查询成功", list);
    }

    @GetMapping("/search/{page}/{size}")
    public PageResult<Brand> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        PageResult<Brand> pageResult = brandService.findPage(page, size);
        return pageResult;
    }

    @PostMapping("/search/{page}/{size}")
    public PageResult<Brand> findPage(@RequestBody(required = false) Brand brand,
                                      @PathVariable("page") int page,
                                      @PathVariable("size") int size) {
        PageResult<Brand> pageResult = brandService.findPage(brand, page, size);
        return pageResult;
    }

    @GetMapping("/selectOptions")
    public List<Map> selectOptions() {
        return brandService.selectOptions();
    }

}
