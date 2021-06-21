package com.huzi.provider.serviceImpl;

import com.huzi.common.PurchaseOrderStatus;
import com.huzi.domain.*;
import com.huzi.provider.dao.InventoryDao;
import com.huzi.provider.dao.OrderDao;
import com.huzi.provider.dao.SaleDao;
import com.huzi.service.OrderService;
import com.huzi.service.ReserveService;
import com.huzi.warehouseinterface.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;




@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private OrderManagementService orderManagementService;


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

/*逻辑：/***调用的service应该在另一个新建的当中
*      （1）处理状态为init的订单：没库存/有足够库存/没有足够库存
*      （2）处理状态为lack的订单：没库存/有足够库存/没有足够库存
*      （3）根据sale状态，处理order状态
* */
    //预定库存(支持部分预订)------------------------------------------------------
    @Override
    public int reserve1() {

        //(1)查所有order的状态为init\LACK的订单
        List<UserOrder> orderLists =orderDao.selectOrderByState2();
        for(UserOrder order: orderLists){
            System.out.println("order" + order.getOrderId());
            //在t_order表中获取warehouseId
            Integer warehouseId = order.getWarehouseId();
            //在t_sale表中通过orderId，获取所有有关的SALE对象,List<Sale>
            Sale sale = new Sale();
            sale.setOrderId(order.getOrderId());
            List<Sale> saleList = saleDao.selectSaleByOrderId(sale);
            //跳转至reserveService处理预订，加事务
            String orderState = reserveService.reserve(saleList,warehouseId,order);
            if (PurchaseOrderStatus.SUCCESS.name().equals(orderState)) {
                //通知仓库项目
                com.huzi.warehouseinterface.domain.UserOrder userOrder = new com.huzi.warehouseinterface.domain.UserOrder();
                userOrder.setUserOrderId(order.getOrderId());
               orderManagementService.insertOrderManagement(userOrder);
            }
        }
        return 0;

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


        //判断order对象，状态是否为success 或者  init lack
        if (!PurchaseOrderStatus.SUCCESS.name().equals(userOrder.getOrderState())) {
            return 2;                   // tip = "此order状态为init/lack" ;
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
    public void orderDelivery(UserOrder userOrder) {

        //第一步：根据orderId，查找对应的List<Sale>
        Integer orderId = userOrder.getOrderId();
        List<Sale> saleList = saleDao.selectSaleByOrderIdNumber(orderId);

        //第二步：循环遍历saleList，取出每个sale的skuId，warehouseId，amount
        for (Sale sale :saleList){
            //按skuId和warehouseId去扣除对应Inventory的物理库存
            InventoryParam inventoryParam = new InventoryParam();
            inventoryParam.setSkuId(sale.getSkuId());
            inventoryParam.setWarehouseId(sale.getWarehouseId());
            inventoryParam.setPhysicalInventoryAdd(sale.getAmount());
            inventoryDao.updateInventoryCutPhysical(inventoryParam);
        }

        /*//根据orderId找出相关的详情信息
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

        return 0;  // result = "成功出库";*/
    }



//---------------------------------------------------------------------------------------

    //仓库自检（自动） 检测inventory的更新时间是否在10分钟内有过变动
    @Override
    public void checkInventoryUpdateTime() {
        Inventory inventory = inventoryDao.selectInventoryByUpdateTime();
        if (inventory != null ){
            reserve1();
        }
    }



}


