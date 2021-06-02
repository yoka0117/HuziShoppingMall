package com.huzi.provider.serviceImpl;


import com.alibaba.dubbo.config.annotation.Service;
import com.huzi.common.PurchaseOrderStatus;
import com.huzi.domain.*;
import com.huzi.provider.serviceImpl.exception.BusinessException;
import com.huzi.provider.dao.InventoryDao;
import com.huzi.provider.dao.OrderDao;
import com.huzi.provider.dao.SaleDao;
import com.huzi.service.OrderService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Service(interfaceClass = OrderService.class,version = "1.0.0",timeout = 15000)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private InventoryDao inventoryDao;


    //添加订单表-----------------------------------------------------------------
    @Override
    public int addOrder(WarehouseRegionMapper warehouseRegionMapper) {
        //新建用户订单对象
        UserOrder order = new UserOrder();
        //添加仓库id
        order.setWarehouseId(warehouseRegionMapper.getWarehouseId());
        //添加地区id
        order.setRegionId(warehouseRegionMapper.getRegionId());
        //更改订单状态、创建时间
        order.setOrderState(PurchaseOrderStatus.INIT.name());
        order.setOrderCreateTime(new Date());
        //创建，并返回orderId
        if (orderDao.addOrder(order) > 0) {
            return order.getOrderId();
        }

        return 0;
    }


    //预定库存(支持部分预订)------------------------------------------------------
    @Override
    public int reserve() {

        //查所有order的状态
        List<UserOrder> orderList = orderDao.selectOrderAll();

        for (UserOrder order : orderList) {
            //获取订单状态
            String orderState = order.getOrderState();
            //（1）如果order的状态是SUCCESS
            if (PurchaseOrderStatus.SUCCESS.name().equals(orderState)) {
                return 2;// tip = "order已经为预定状态";
            }
            //（2）如果order的状态是INIT（需处理）
            if (PurchaseOrderStatus.INIT.name().equals(orderState)) {
                //在t_order表中获取warehouseId
                Integer warehouseId = order.getWarehouseId();
                //在t_sale表中通过orderId，获取所有有关的SALE对象,List<Sale>
                Sale sale = new Sale();
                sale.setOrderId(order.getOrderId());
                List<Sale> saleList = saleDao.selectSaleByOrderId(sale);
                //判断一下，Sale是否存在
                if (saleList.size() == 0) return 3; //tip = "sale不存在";
                //获取所有的skuId和amount
                for (Sale sales : saleList) {
                    Integer skuId = sales.getSkuId();
                    Integer amount = sales.getAmount();
                    //查询有关库存Inventory是否存在
                    Inventory inventory = new Inventory();
                    inventory.setSkuId(skuId);
                    inventory.setWarehouseId(warehouseId);
                    Inventory it = inventoryDao.selectInventory(inventory);
                    if (it == null) return 4; // tip = "库存不存在";
                    //**准备进行库存操作**
                    //根据skuId和warehouseId，先判断real库存数量是否为0。如果为0，则直接更新sale表中的状态为缺货
                    Integer realInventory = it.getRealInventory();
                    if (realInventory == 0) {
                        sales.setSaleState(PurchaseOrderStatus.LACK.name());
                        sales.setAlready(0);
                        //更改为缺货状态，并填写已经准备好的数量
                        saleDao.updateSaleState(sales);
                        //
                    }
                    //如果real库存不为0
                    //将sale属性，装进InventoryParam对象中
                    InventoryParam inventoryParam = new InventoryParam();
                    inventoryParam.setWarehouseId(warehouseId);
                    inventoryParam.setSkuId(skuId);
                    inventoryParam.setRealInventoryAdd(amount);
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
                        sales.setAlready(realInventory);
                        saleDao.updateSaleState(sales);
                    }
                }
            }

            //(3)如果order的状态是LACK（需处理）
            if (PurchaseOrderStatus.LACK.name().equals(orderState)) {
                //在t_order表中获取warehouseId
                Integer warehouseId = order.getWarehouseId();
                //在t_sale表中通过orderId，获取所有有关的SALE对象,List<Sale>
                Sale sale = new Sale();
                sale.setOrderId(order.getOrderId());
                List<Sale> saleList = saleDao.selectSaleByOrderId(sale);
                //循环遍历
                for (Sale sales : saleList) {
                    //看一下哪个sale的状态是LACK
                    if (PurchaseOrderStatus.LACK.name().equals(sales.getSaleState())) {
                        //如果是LACK走此处：拿到此sale的skuId，amount，already的值
                        Integer skuId = sales.getSkuId();
                        Integer amount = sales.getAmount();
                        Integer already = sales.getAlready();
                        //查询有关库存是否存在
                        Inventory inventory = new Inventory();
                        inventory.setSkuId(skuId);
                        inventory.setWarehouseId(warehouseId);
                        Inventory it = inventoryDao.selectInventory(inventory);
                        if (it == null) return 4;    //  tip = "库存不存在";
                        //此时取出仓库里真实库存的值，看看是否为0
                        Integer realInventory = it.getRealInventory();
                        if (realInventory == 0) return 6; // tip = "real库存距离上次缺货还没有上新";
                        //如果不为0，则开始扣库存
                        //将sale属性，装进InventoryParam对象中
                        InventoryParam inventoryParam = new InventoryParam();
                        inventoryParam.setWarehouseId(warehouseId);
                        inventoryParam.setSkuId(skuId);
                        inventoryParam.setRealInventoryAdd(amount - already);
                        int result = inventoryDao.updateInventoryCutReal(inventoryParam);
                        //判断结果
                        if (result > 0) {
                            //如果成功,将sale表中对应的状态改为success
                            sales.setSaleState(PurchaseOrderStatus.SUCCESS.name());
                            sales.setAlready(amount);
                            saleDao.updateSaleState(sales);
                        } else {
                            //如果失败
                            //1.此时应当先扣除已有的部分
                            inventoryParam.setRealInventoryAdd(realInventory);
                            inventoryDao.updateInventoryCutReal(inventoryParam);
                            //2.更新已经预约的数量
                            sales.setAlready(realInventory + already);
                            saleDao.updateSaleState(sales);
                        }
                    }
                }
            }
            //检查所有的sale状态，如果都为success，则改变order订单的状态
            Sale sale = new Sale();
            sale.setOrderId(order.getOrderId());
            List<Sale> saleList = saleDao.selectSaleByOrderId(sale);
            for (Sale sales : saleList) {
                if (!PurchaseOrderStatus.SUCCESS.name().equals(sales.getSaleState())) {
                    //throw new BusinessException(sales.getOrderId().toString(),"sale未完单");
                    order.setOrderState(PurchaseOrderStatus.LACK.name());
                    orderDao.updateOrder(order);
                    return 7;    // tip = "此订单还有预约没有完全完成";
                }
            }

            //走到这里，所有sale状态都为success了
            order.setOrderState(PurchaseOrderStatus.SUCCESS.name());
            orderDao.updateOrder(order);


        }

        return 5;   //预订成功

    }
    //----------------------------------------------------------------------------------------


    //取消预订
    @Override
    public int cancelReserve(UserOrder order) {

        //查找userOrder订单
        UserOrder userOrder = orderDao.selectOrder(order);

        //判断order对象是否为空
        if (userOrder == null) {
            return 1;
        }   // tip = "此order不存在";
        //拿到仓库id
        Integer warehouseId = userOrder.getWarehouseId();


        //判断order对象，状态是否为success 或者  init
        if (PurchaseOrderStatus.INIT.name().equals(userOrder.getOrderState())) {
            return 2;                   // tip = "此order状态为init" ;
        }
        //如果为success
        else if (PurchaseOrderStatus.SUCCESS.name().equals(userOrder.getOrderState())) {
            //改为init状态
            userOrder.setOrderState(PurchaseOrderStatus.INIT.name());
            orderDao.updateOrder(userOrder);


            //通过orderId，查找对应的产品数量amount
            Sale sale = new Sale();
            sale.setOrderId(userOrder.getOrderId());
            //通过orderId，查sale
            List<Sale> saleList = saleDao.selectSaleByOrderId(sale);

            //将sale集合遍历，
            for (Sale sales : saleList) {
                Integer skuId = sales.getSkuId();
                Integer amount = sales.getAmount();
                //将里面的skuId，数量取出，加回库存中
                InventoryParam inventoryParam = new InventoryParam();
                inventoryParam.setSkuId(skuId);
                inventoryParam.setWarehouseId(warehouseId);
                inventoryParam.setRealInventoryAdd(amount);
                inventoryDao.updateInventoryAdd(inventoryParam);
            }
        }
        return 3;   // tip = "取消预订成功";
    }


    //------------------------------------------------------------------
    //订单出库确认
    @Override
    public int orderDelivery(Integer orderId, List<OrderDelivery> orderDelivery) {
        //根据orderId找出相关的详情信息
        UserOrder order = new UserOrder();
        order.setOrderId(orderId);
        UserOrder order1 = orderDao.selectOrder(order);
        //验证订单是否存在
        if (order1 == null) return 1;    //result="order订单不存在";
        //验证订单状态是不是属于预订成功
        if (!PurchaseOrderStatus.SUCCESS.name().equals(order1.getOrderState())) {
            return 2; //result = "订单状态不属于预订成功";
        }
        //验证出库详情跟预订详情是否完全一致,第一种方法
        for (OrderDelivery orderDelivery1 : orderDelivery) {
            Sale sale = new Sale();
            sale.setSkuId(orderDelivery1.getSkuId());
            sale.setAmount(orderDelivery1.getAmount());
            sale.setOrderId(orderId);
            if (saleDao.selectSaleBySkuIdAmount(sale) == null) {
                return 3;  // result = "出货单与详情单不同";
            }
        }

        //验证出库详情跟预订详情是否完全一致,第二种方法
        List<String> orderDeliveryList = new ArrayList<>();
        List<String> salesList = new ArrayList<>();
        for (OrderDelivery orderDelivery1 :orderDelivery){
            String skuId = String.valueOf(orderDelivery1.getSkuId());
            String amount = String.valueOf(orderDelivery1.getAmount());
            String orderId1 = String.valueOf(orderId);
            orderDeliveryList.add(skuId);
            orderDeliveryList.add(amount);
            orderDeliveryList.add(orderId1);

            Sale sale = new Sale();
            sale.setOrderId(orderId);
            List<Sale> sales = saleDao.selectSaleByOrderId(sale);
            for (Sale sale1 : sales){
                String saleSkuId = String.valueOf(sale1.getSkuId());
                String saleAmount = String.valueOf(sale1.getSkuId());
                String saleOrderId = String.valueOf(sale1.getOrderId());
                salesList.add(saleSkuId);
                salesList.add(saleAmount);
                salesList.add(saleOrderId);

                //看看是否是包含关系
                if (!orderDeliveryList.contains(salesList)){
                    return 3;  // result = "出货单与详情单不同";
                }
            }
        }
        //库存扣减物理库存
        Sale sale = new Sale();
        sale.setOrderId(orderId);
        List<Sale> saleList = saleDao.selectSaleByOrderId(sale);
        for (Sale sales : saleList){
            InventoryParam ip = new InventoryParam();
            ip.setSkuId(sales.getSkuId());
            ip.setPhysicalInventoryAdd(sales.getAmount());
            ip.setWarehouseId(order1.getWarehouseId());
            inventoryDao.updateInventoryCutPhysical(ip);
        }
        //订单改成出库成功。
        order1.setOrderState(PurchaseOrderStatus.DELIVERY.name());
        orderDao.updateOrder(order1);

        return 0;  // result = "成功出库";
    }




//---------------------------------------------------------------------------------------

    //预订库存（支持部分预订） 新版
    @Override
    public int reserve2() {
        //将所有状态为INIT的Order取出来，处理
        UserOrder order = new UserOrder();
        order.setOrderState(PurchaseOrderStatus.INIT.name());
        //通过状态查找UserOrder所有状态为INIT
        List<UserOrder> orderList = orderDao.selectOrderByState(order);
        //如果没有
        if (orderList == null ) return 0;//    0 =  没有为init的UserOrder

        //如果有：
        for (UserOrder orders : orderList){
            //将对应orderId的SALE取出来
            int orderId = orders.getOrderId();
            Sale sale = new Sale();
            sale.setOrderId(orderId);
            List<Sale> saleList = saleDao.selectSaleByOrderId(sale);
            //将每个sale的skuId和仓库Id
            for (Sale sales : saleList){
                Integer skuId = sales.getSkuId();
                Integer amount =  sales.getAmount();
                Integer warehouseId =  sale.getWarehouseId();
                //用Inventory对象包装
                Inventory inventory = new Inventory();
                inventory.setSkuId(skuId);
                inventory.setWarehouseId(warehouseId);
                //将此对象，传入,找到对应的Inventory
                Inventory it = inventoryDao.selectInventory(inventory);
                if (it == null) return 1;   //1 = 没有对应的库存
                //拿inventory映射类去扣减库存
                InventoryParam ip = new InventoryParam();
                ip.setSkuId(it.getSkuId());
                ip.setWarehouseId(it.getWarehouseId());
                ip.setRealInventoryAdd(amount);
                int result = inventoryDao.updateInventoryCutReal(ip);
                //判断
                if (result >0 ){
                    //成功
                    sales.setAlready(amount);
                    sales.setSaleState(PurchaseOrderStatus.SUCCESS.name());
                    saleDao.updateSaleState(sales);
                }else {
                    //不成功，证明real库存不够扣
                    //1.此时应当先扣除已有的部分
                    ip.setRealInventoryAdd(it.getRealInventory());
                    inventoryDao.updateInventoryCutReal(ip);
                    //2.再将sale表中的状态改为LACK，并标注已经预约的数量
                    sales.setSaleState(PurchaseOrderStatus.LACK.name());
                    sales.setAlready(it.getRealInventory());
                    saleDao.updateSaleState(sales);
                }
            }
            //检查所有的sale状态，如果都为success，则改变order订单的状态
            Sale sale1 = new Sale();
            sale1.setOrderId(orderId);
            List<Sale> saleList1 = saleDao.selectSaleByOrderId(sale1);
            for(Sale sales : saleList1){
                if(!PurchaseOrderStatus.SUCCESS.name().equals(sales.getSaleState())){
                    return  2 ;// tip = "此订单还有预约没有完全完成";
                }
                order.setOrderState(PurchaseOrderStatus.SUCCESS.name());
                orderDao.updateOrder(order);
            }
        }
        return 3;    //预订成功

    }

    @Override
    public int reserve3() {
        return 0;
    }


}


