package com.yan.security.monitorSystem.repositories;


import com.yan.security.monitorSystem.models.DeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceLogRepository extends JpaRepository<DeviceLog, Long> {


    List<DeviceLog> findAllByOrderByTimestampDesc();
}
