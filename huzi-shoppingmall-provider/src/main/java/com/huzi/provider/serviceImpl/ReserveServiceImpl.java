package com.huzi.provider.serviceImpl;

import com.huzi.common.PurchaseOrderStatus;
import com.huzi.domain.Inventory;
import com.huzi.domain.InventoryParam;
import com.huzi.domain.Sale;
import com.huzi.domain.UserOrder;
import com.huzi.provider.dao.InventoryDao;
import com.huzi.provider.dao.OrderDao;
import com.huzi.provider.dao.SaleDao;
import com.huzi.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReserveServiceImpl implements ReserveService {

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private OrderDao orderDao;

    @Transactional
    @Override
    public String reserve(List<Sale> saleList, Integer warehouseId, UserOrder order) {
        //布尔标记//
        Boolean tip = true;
        //获取所有的skuId和amount
        for (Sale sales : saleList) {
            System.out.println("sale " + sales.getSaleId());
            Integer skuId = sales.getSkuId();
            Integer amount = sales.getAmount();
            Integer already = sales.getAlready();
            //查询有关库存Inventory是否存在
            Inventory inventory = new Inventory();
            inventory.setSkuId(skuId);
            inventory.setWarehouseId(warehouseId);
            Inventory it = inventoryDao.selectInventory(inventory);
            if (null == it) {
                continue;
            }
            //**准备进行库存操作**
            //根据skuId和warehouseId，先判断real库存数量是否为0。如果为0，则直接更新sale表中的状态为缺货
            Integer realInventory = it.getRealInventory();
            if (realInventory == 0) {
                sales.setSaleState(PurchaseOrderStatus.LACK.name());
                sales.setAlready(already);
                //更改为缺货状态，并填写已经准备好的数量
                saleDao.updateSaleState(sales);
                tip = false ;
            } else {
                //real库存不为0
                //将sale属性，装进InventoryParam对象中
                InventoryParam inventoryParam = new InventoryParam();
                inventoryParam.setWarehouseId(warehouseId);
                inventoryParam.setSkuId(skuId);
                inventoryParam.setRealInventoryAdd(amount - already);
                //扣减真实库存,返回成功数
                int result = inventoryDao.updateInventoryCutReal(inventoryParam);
                //查看是否成功
                if (result > 0) {
                    //成功,将sale表中对应的状态改为success、更新准备好货的数量
                    sales.setSaleState(PurchaseOrderStatus.SUCCESS.name());
                    sales.setAlready(amount);
                    saleDao.updateSaleState(sales);
                } else {
                    //不成功，证明real库存不够扣
                    //1.此时应当先扣除已有的部分
                    inventoryParam.setRealInventoryAdd(realInventory);
                    inventoryDao.updateInventoryCutReal(inventoryParam);
                    //2.再将sale表中的状态改为LACK，并标注已经预约的数量
                    sales.setSaleState(PurchaseOrderStatus.LACK.name());
                    sales.setAlready(realInventory + already);
                    saleDao.updateSaleState(sales);
                    tip = false ;
                }
            }
        }
        //更改订单order状态
        if (tip != false){
            order.setOrderState(PurchaseOrderStatus.SUCCESS.name());
            orderDao.updateOrder(order);
            return order.getOrderState();
        }else {
            order.setOrderState(PurchaseOrderStatus.LACK.name());
            orderDao.updateOrder(order);
            return order.getOrderState();
        }
    }
}
