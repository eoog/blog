package com.www.timesaleservice.repository;

import com.www.timesaleservice.domain.TimeSaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSaleOrderRepository extends JpaRepository<TimeSaleOrder, Long> {

}
