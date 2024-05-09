package com.atguigu.hospital.service;

import java.io.IOException;
import java.util.Map;

public interface HospitalService {

    /**
     * Appointment Booking
     * @param paramMap
     * @return
     */
    Map<String, Object> submitOrder(Map<String, Object> paramMap);

    /**
     * Update Payment Status
     * @param paramMap
     */
    void updatePayStatus(Map<String, Object> paramMap);

    /**
     * Update Cancellation Status
     * @param paramMap
     */
    void updateCancelStatus(Map<String, Object> paramMap);


}
