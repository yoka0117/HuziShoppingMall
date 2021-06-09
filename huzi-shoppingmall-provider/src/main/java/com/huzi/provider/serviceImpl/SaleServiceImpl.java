package com.huzi.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.domain.Sale;
import com.huzi.provider.dao.SaleDao;
import com.huzi.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;



@Component
@Service(interfaceClass = SaleService.class,version = "1.0.0",timeout = 15000)
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleDao saleDao;




    //添加销售表
    @Override
    public int addSale(List<Sale> saleList) {
        for (Sale sale : saleList) {
            //添加销售表
            System.out.println("--------------------------");
            int result = saleDao.addSale(sale);
            System.out.println("--------------------------");
            if(result == 0){
                return 0;
            }
        }
        return 1;
    }
}
