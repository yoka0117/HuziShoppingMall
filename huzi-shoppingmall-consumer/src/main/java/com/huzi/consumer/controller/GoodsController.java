package com.huzi.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.huzi.consumer.aspect.annotation.MyAnnotation;
import com.huzi.domain.Goods;
import com.huzi.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


//***商品有关功能*** 2021.6.8
@Controller
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    //通过ID查询商品
    @MyAnnotation(value = "goods_service")
    @RequestMapping("/goodsById")
    public String selectGoodsById(Integer goodsId, Model model){

        Goods goods = goodsService.selectGoodsById(goodsId);
        if(goods != null){
            model.addAttribute("goods",goods);
            return "goods/goods";
        } else {
            return "goods/goodsError";
        }
    }

    //添加goods商品
    @RequestMapping("/insertGoods")
    public ModelAndView insertGoods(Goods goods){
        ModelAndView mv = new ModelAndView();
        String tip = "添加失败";
        Integer num = goodsService.insertGoods(goods);
        if(num > 0 ){
            tip = "商品添加成功";
        }
        mv.addObject("result",tip);
        mv.setViewName("goods/insertGoods");
        return mv;
    }

    //删除商品
    @RequestMapping("/deleteGoods")
    public ModelAndView deleteGoods(Integer goodsId ){
        ModelAndView mv = new ModelAndView();
        String result = "删除成功";
        if (goodsService.deleteGoodsById(goodsId)>0){
            result = "删除成功";
        }
        mv.addObject("result",result);
        mv.setViewName("goods/deleteGoods");
        return mv;
    }


}
