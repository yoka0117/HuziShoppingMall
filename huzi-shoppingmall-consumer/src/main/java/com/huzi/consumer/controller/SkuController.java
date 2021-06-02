package com.huzi.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.common.CommonResult;
import com.huzi.domain.Sku;
import com.huzi.domain.SkuGoods;
import com.huzi.service.GoodsService;
import com.huzi.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/Sku")
public class SkuController {


    @Reference(interfaceClass = SkuService.class,version = "1.0.0" ,check = false)
    private SkuService skuService;

        //添加SKU
        @RequestMapping("/insertSKU")
        public ModelAndView insertSKU(Sku sku){
            ModelAndView mv = new ModelAndView();
            String tip = "添加sku失败";
            if(sku.getGoodsId() != 0 && sku.getSkuColor() !=null && sku.getSkuSize() != 0){
                if(skuService.insertSKU(sku) > 0){
                    tip = "添加sku成功";
                }
            }
            mv.addObject("result",tip);
            mv.setViewName("sku/insertSku");
            return mv;
        }



        //查询所有Sku + goods
        @RequestMapping("/selectGoodsAndSku")
        public ModelAndView selectGoodsAndSku(){
            ModelAndView mv = new ModelAndView();
            List<SkuGoods> list = skuService.selectGoodsAndSku();
            CommonResult result = new CommonResult();
            if (list != null){
                result.setCode("SUCCESS");
                result.setMsg("成功");
                result.setResult(true);
                result.setData(list);
            }
            mv.addObject("result",result);
            mv.setViewName("sku/SkuAndGoods");
            return mv;
        }


























}