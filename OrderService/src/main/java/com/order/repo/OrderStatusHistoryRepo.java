package com.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.entity.OrderStatusHistory;

@Repository
public interface OrderStatusHistoryRepo extends JpaRepository<OrderStatusHistory, Integer> {

}
