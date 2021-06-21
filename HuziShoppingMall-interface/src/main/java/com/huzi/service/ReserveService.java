package com.huzi.service;

import com.huzi.domain.Sale;
import com.huzi.domain.UserOrder;

import java.util.List;

public interface ReserveService {

    public String reserve(List<Sale> saleList , Integer warehouseId, UserOrder order);
}
