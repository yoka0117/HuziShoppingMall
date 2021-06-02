package com.huzi.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.common.PurchaseOrderStatus;
import com.huzi.domain.Inventory;
import com.huzi.domain.InventoryParam;
import com.huzi.domain.OrderDetails;
import com.huzi.domain.PurchaseOrder;
import com.huzi.provider.dao.InventoryDao;
import com.huzi.provider.dao.PurchaseOrderDao;
import com.huzi.service.InventoryService;
import com.huzi.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
@Service(interfaceClass = PurchaseOrderService.class,version = "1.0.0",timeout = 15000)
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private InventoryDao inventoryDao;


    //新增采购单------------------------------------------------------------------
    //第一步：新增空白采购单（赋值创建时间、订单状态）
    @Override
    public int insertPurchase(PurchaseOrder purchaseOrder) {
        purchaseOrder.setPurchaseState(PurchaseOrderStatus.INIT.name());
        purchaseOrder.setPurchaseCreateTime(new Date());
        if (purchaseOrderDao.insertPurchase(purchaseOrder) > 0) {
            return purchaseOrder.getPurchaseId();
        } else {
            return 0;
        }
    }

    //第二步：添加采购单详情
    @Override
    public int insertOrderDetails(List<OrderDetails> orderDetailsList) {
        int num;
        for (OrderDetails orderDetails : orderDetailsList) {
            num = purchaseOrderDao.insertDetails(orderDetails);
            if (num == 0) {
                return 0;
            }
        }
        return 1;
    }
    //-------------------------------------------------------------------------------


    //根据采购单号查询采购单详情
    @Override
    public PurchaseOrder selectPurchaseOrderAndDetails(Integer purchaseId) {
        //第一步：传入采购单号查询采购表（可查询状态、创建时间）
        PurchaseOrder purchaseOrder = purchaseOrderDao.selectPurchaseOrderById(purchaseId);

        if (purchaseOrder != null) {
            //第二步：根据采购单号查询采购详情表（skuId、warehouseId、数量）
            List<OrderDetails> orderDetailsList = purchaseOrderDao.selectOrderDetailsByPurchaseId(purchaseId);
            purchaseOrder.setOrderDetails(orderDetailsList);
        }
        return purchaseOrder;

    }


    // 作废订单(根据采购单编号)
    @Override
    public String invalidPurchase(Integer purchaseId) {
        //第一步：验证是否存在
        PurchaseOrder purchaseOrder = purchaseOrderDao.selectPurchaseOrderById(purchaseId);
        //验证单号是否存在
        if (purchaseOrder == null) {
            return "没有此单号，请重新输入";
        }
        //检查订单状态是否已经FINISH
        if (PurchaseOrderStatus.FINISH.name().equals(purchaseOrder.getPurchaseState())) {
            return "订单已经完成，操作无效";
        } else if (PurchaseOrderStatus.INVALID.name().equals(purchaseOrder.getPurchaseState())) {
            return "订单已经作废，操作无效";
        }

        //第二步：过滤完成后的订单都为INIT可操作订单，更新时间、更改状态
        purchaseOrder.setPurchaseUpdateTime(new Date());
        purchaseOrder.setPurchaseState(PurchaseOrderStatus.INVALID.name());
        if (purchaseOrderDao.updatePurchase(purchaseOrder) > 0) {
            return "作废成功";
        }
        return "作废失败";
    }


    //完结采购单***
    @Override
    public String completePurchase(OrderDetails orderDetails) {
        String tip = "";
        //0.拿到订单编号
        int purchaseId = orderDetails.getPurchaseId();
        //1通过订单编号，查询(状态)
        PurchaseOrder purchaseOrder = purchaseOrderDao.selectPurchaseOrderById(purchaseId);
        //2查询订单状态 以及 是否为null
        if (purchaseOrder == null) {
            return "没有此单号，请重新输入";
        }
        if (PurchaseOrderStatus.FINISH.name().equals(purchaseOrder.getPurchaseState())) {
            return "订单已经完成，操作无效";
        } else if (PurchaseOrderStatus.INVALID.name().equals(purchaseOrder.getPurchaseState())) {
            return "订单已经作废，操作无效";
        }

        //3状态为INIT走以下流程：
        //通过skuId+仓库id去查库存id,如果有就增加库存，没有就新建库存（操作库存）
        //获取skuId、仓库id
        List<OrderDetails> orderDetailsList = purchaseOrderDao.selectOrderDetailsByPurchaseId(purchaseId);

        for (OrderDetails orderDetailsTrue : orderDetailsList) {
            Integer skuId = orderDetailsTrue.getSkuId();
            Integer warehouseId = orderDetailsTrue.getWarehouseId();
            //查询库存id
            Inventory inventory = new Inventory();
            inventory.setSkuId(skuId);
            inventory.setWarehouseId(warehouseId);
            Integer inventoryId = inventoryDao.selectInventory(inventory).getInventoryId();
            //如果有库存单---直接增加库存
            if (inventoryId != null) {
                int amount = orderDetailsTrue.getAmount();
                InventoryParam inventoryParam = new InventoryParam();
                inventoryParam.setInventoryId(inventoryId);
                inventoryParam.setPhysicalInventoryAdd(amount);
                inventoryParam.setRealInventoryAdd(amount);
                Integer result = inventoryDao.updateInventory(inventoryParam);
                if (result > 0) {
                    //更改订单的状态(完结、更新时间)
                    purchaseOrder.setPurchaseUpdateTime(new Date());
                    purchaseOrder.setPurchaseState(PurchaseOrderStatus.FINISH.name());
                    purchaseOrderDao.updatePurchase(purchaseOrder);
                } else {
                    return "更新失败";
                }
            }//如果没有库存单--先新建，再更新
            else {
                //新建
                Inventory inventoryTwo = new Inventory();
                inventory.setSkuId(orderDetailsTrue.getSkuId());
                inventory.setWarehouseId(orderDetailsTrue.getWarehouseId());
                inventoryDao.insertInventory(inventoryTwo);
                //更新
                int amount = orderDetailsTrue.getAmount();
                InventoryParam inventoryParam = new InventoryParam();
                inventoryParam.setInventoryId(inventoryId);
                inventoryParam.setPhysicalInventoryAdd(amount);
                inventoryParam.setRealInventoryAdd(amount);
                Integer result = inventoryDao.updateInventory(inventoryParam);
                //状态
                if (result > 0) {
                    purchaseOrder.setPurchaseUpdateTime(new Date());
                    purchaseOrder.setPurchaseState(PurchaseOrderStatus.FINISH.name());
                    purchaseOrderDao.updatePurchase(purchaseOrder);
                } else {
                    return "更新失败";}
            }
        }
                return "更新成功";
    }


}
