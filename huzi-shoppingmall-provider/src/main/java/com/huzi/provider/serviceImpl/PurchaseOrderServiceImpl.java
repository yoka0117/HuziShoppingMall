package com.huzi.provider.serviceImpl;

import com.huzi.common.PurchaseOrderStatus;
import com.huzi.domain.Inventory;
import com.huzi.domain.InventoryParam;
import com.huzi.domain.OrderDetails;
import com.huzi.domain.PurchaseOrder;
import com.huzi.provider.dao.InventoryDao;
import com.huzi.provider.dao.PurchaseOrderDao;
import com.huzi.service.PurchaseOrderService;
import com.huzi.warehouseinterface.domain.PurchaseNotice;
import com.huzi.warehouseinterface.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderDao purchaseOrderDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private OrderManagementService orderManagementService;

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
        //准备一个List，装对象，发往仓库系统
        List<PurchaseNotice> purchaseNoticeList = new ArrayList<>();
        for (OrderDetails orderDetails : orderDetailsList) {
            //加入本地进货单表中
             purchaseOrderDao.insertDetails(orderDetails);
             //传入仓库系统进货表t_purchaseNotice中
            //需要属性：商城进货单id，详情单id,skuId，仓库id，数量。 加入仓库系统的PurchaseNotice对象中，传入仓库
            PurchaseNotice purchaseNotice = new PurchaseNotice();
            purchaseNotice.setPurchaseId(orderDetails.getPurchaseId());
            purchaseNotice.setPurchaseDetailsId(orderDetails.getOrderDetailsId());
            purchaseNotice.setSkuId(orderDetails.getSkuId());
            purchaseNotice.setWarehouseId(orderDetails.getWarehouseId());
            purchaseNotice.setAmount(orderDetails.getAmount());
            purchaseNoticeList.add(purchaseNotice);
        }
        //将List，发往仓库系统
        orderManagementService.purchaseNotice(purchaseNoticeList);

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
    public void completePurchase(List<OrderDetails> orderDetailsList) {
        String tip = "";

        for (OrderDetails orderDetails : orderDetailsList){
            //0.拿到订单编号
            int purchaseId = orderDetails.getPurchaseId();
            //1通过订单编号，查询(状态)
            PurchaseOrder purchaseOrder = purchaseOrderDao.selectPurchaseOrderById(purchaseId);
            //2查询订单状态 以及 是否为null
            if (purchaseOrder == null) {
                System.out.println("没有此单号，" + purchaseId );
            }
            if (PurchaseOrderStatus.FINISH.name().equals(purchaseOrder.getPurchaseState())) {
                System.out.println( "订单"+ purchaseId +"已经完成，操作无效");
            } else if (PurchaseOrderStatus.INVALID.name().equals(purchaseOrder.getPurchaseState())) {
                System.out.println("订单" + purchaseId +"已经作废，操作无效");
            }
            //3状态为INIT走以下流程：
            //通过skuId+仓库id去查库存id,如果有就增加库存，没有就新建库存（操作库存）
            //获取skuId、仓库id
            Integer skuId = orderDetails.getSkuId();
            Integer warehouseId = orderDetails.getWarehouseId();
            //查询库存id是否存在
            Inventory inventory = new Inventory();
            inventory.setSkuId(skuId);
            inventory.setWarehouseId(warehouseId);
            Inventory it = inventoryDao.selectInventory(inventory);
            Integer inventoryId = it.getInventoryId();
            //（1）库存存在
            if (it != null ){
                int amount = orderDetails.getAmount();
                InventoryParam inventoryParam = new InventoryParam();
                inventoryParam.setInventoryId(inventoryId);
                inventoryParam.setPhysicalInventoryAdd(amount);
                inventoryParam.setRealInventoryAdd(amount);
                Integer result = inventoryDao.updateInventory(inventoryParam);
                //插入成功
                if (result>0){
                    purchaseOrder.setPurchaseUpdateTime(new Date());
                    purchaseOrder.setPurchaseState(PurchaseOrderStatus.FINISH.name());
                    purchaseOrderDao.updatePurchase(purchaseOrder);
                }else { //或失败
                    System.out.println("订单号"+ purchaseId + "更新库存失败");
                }
            }else {
                //（2）库存不存在，先新建，再更新
                //新建
                Inventory inventoryTwo = new Inventory();
                inventory.setSkuId(orderDetails.getSkuId());
                inventory.setWarehouseId(orderDetails.getWarehouseId());
                inventoryDao.insertInventory(inventoryTwo);
                //更新
                int amount = orderDetails.getAmount();
                InventoryParam inventoryParam = new InventoryParam();
                inventoryParam.setInventoryId(inventoryId);
                inventoryParam.setPhysicalInventoryAdd(amount);
                inventoryParam.setRealInventoryAdd(amount);
                Integer result = inventoryDao.updateInventory(inventoryParam);
                //成功
                if (result > 0) {
                    purchaseOrder.setPurchaseUpdateTime(new Date());
                    purchaseOrder.setPurchaseState(PurchaseOrderStatus.FINISH.name());
                    purchaseOrderDao.updatePurchase(purchaseOrder);
                } else {//失败
                    System.out.println("采购单" + purchaseId + "相关库存，新建加入失败");
                }
            }

        }

    }


}
