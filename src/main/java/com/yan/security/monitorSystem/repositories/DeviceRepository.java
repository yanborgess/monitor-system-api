package com.yan.security.monitorSystem.repositories;

import com.yan.security.monitorSystem.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long> {


    List<Device>findByActiveFalse();
}
