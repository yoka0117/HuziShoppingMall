package com.huzi.provider.serviceImpl;

import com.huzi.common.PurchaseOrderStatus;
import com.huzi.domain.Inventory;
import com.huzi.domain.InventoryParam;
import com.huzi.domain.OrderDetails;
import com.huzi.domain.PurchaseOrder;
import com.huzi.provider.dao.InventoryDao;
import com.huzi.provider.dao.PurchaseOrderDao;
import com.huzi.service.GoodsService;
import com.huzi.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private PurchaseOrderDao purchaseOrderDao;


    //添加商品库存
    @Override
    public Integer insertInventory(Inventory inventory) {
        return inventoryDao.insertInventory(inventory);
    }


    //管理员管理订单状态
    @Override
    public String PurchaseOrderByUser(Integer purchaseId, Integer orderDetailsId) {
        //验证采购单/详情表是否存在
        OrderDetails orderDetails = purchaseOrderDao.selectOrderDetails(orderDetailsId);
        PurchaseOrder purchaseOrder = purchaseOrderDao.selectPurchaseOrderById(purchaseId);
        if (orderDetails == null){return "采购详情表单不存在";}
        if(purchaseOrder==null){return  "采购表单不存在";}

        //判断详情单是否属于采购单
        if(orderDetails.getPurchaseId() != purchaseOrder.getPurchaseId()){
            return "采购单与详情表单不对应";
        }

        //查看采购单是否是INIT
        if(PurchaseOrderStatus.INIT.name().equals(purchaseOrder.getPurchaseState())) {
            //查看详情单是否是INIT
            if (PurchaseOrderStatus.INIT.name().equals(orderDetails.getOrderDetailsState())) {
                //获取库存id
                int skuId = orderDetails.getSkuId();
                int warehouseId = orderDetails.getWarehouseId();
                Inventory inventory = new Inventory();
                inventory.setSkuId(skuId);
                inventory.setWarehouseId(warehouseId);
                Inventory inventory1 = inventoryDao.selectInventory(inventory);
                Integer inventoryId = inventory1.getInventoryId();
                if (inventoryId != null) {
                    //如果有庫存单---完结详情单

                    //加库存
                    InventoryParam inventoryParam = new InventoryParam();
                    inventoryParam.setSkuId(orderDetails.getSkuId());
                    inventoryParam.setWarehouseId(orderDetails.getWarehouseId());
                    inventoryParam.setPhysicalInventoryAdd(orderDetails.getAmount());
                    inventoryParam.setRealInventoryAdd(orderDetails.getAmount());
                    inventoryParam.setInventoryUpdateTime(new Date());
                    inventoryDao.updateInventory(inventoryParam);

                    orderDetails.setOrderDetailsState(PurchaseOrderStatus.FINISH.name());
                    purchaseOrderDao.updateDetails(orderDetails);
                }else {
                    //如果没有庫存单---完结详情单
                    //新建
                    Inventory inventoryTwo = new Inventory();
                    inventory.setSkuId(orderDetails.getSkuId());
                    inventory.setWarehouseId(orderDetails.getWarehouseId());
                    inventoryDao.insertInventory(inventoryTwo);
                    //加库存、更新状态、更新时间
                    Integer inventoryId2 =inventoryTwo.getInventoryId();
                    int amount = orderDetails.getAmount();
                    InventoryParam inventoryParam = new InventoryParam();
                    inventoryParam.setInventoryId(inventoryId2);
                    inventoryParam.setPhysicalInventoryAdd(amount);
                    inventoryParam.setRealInventoryAdd(amount);
                    inventoryParam.setInventoryUpdateTime(new Date());
                    inventoryDao.updateInventory(inventoryParam);
                    //更改采购单状态
                    //将状态改为FINISH,更新时间
                    purchaseOrder.setPurchaseUpdateTime(new Date());
                    purchaseOrder.setPurchaseState(PurchaseOrderStatus.FINISH.name());
                    purchaseOrderDao.updatePurchase(purchaseOrder);

                }
            }//查询采购单下面所有详情，如果都完结了，则完结采购单
            List<OrderDetails> orderDetailsList = purchaseOrderDao.selectOrderDetailsByPurchaseId(purchaseId);
            for (OrderDetails od : orderDetailsList){
                if (!PurchaseOrderStatus.FINISH.name().equals(od.getOrderDetailsState())){
                    return "成功完结";
                }
                //完结采购单
                purchaseOrder.setPurchaseState(PurchaseOrderStatus.FINISH.name());
                purchaseOrder.setPurchaseUpdateTime(new Date());
                purchaseOrderDao.updatePurchase(purchaseOrder);
            }
        }

        return "成功完结";
    }



    //--------------------------------------------------------------------

}
