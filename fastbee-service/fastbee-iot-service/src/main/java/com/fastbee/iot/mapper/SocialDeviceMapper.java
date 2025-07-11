package com.fastbee.iot.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fastbee.framework.mybatis.mapper.BaseMapperX;
import com.fastbee.iot.domain.Device;
import com.fastbee.iot.domain.ProductSubGateway;
import com.fastbee.iot.domain.bo.DeviceBo;
import com.fastbee.iot.model.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备Mapper接口
 *
 * @author kerwincui
 * @date 2021-12-16
 */
@Repository
public interface SocialDeviceMapper extends BaseMapperX<Device> {
    /**
     * 查询设备和运行状态
     *
     * @param device 设备对象
     * @return 设备
     */
    public List<SocialDeviceShortOutput> selectDeviceRunningStatusByDeviceList(Device device);


    @Select({
            "select d.device_id, d.device_name, d.product_id, p.product_name,p.device_type, d.tenant_id, d.tenant_name, " +
                    "d.serial_number, d.firmware_version,d.wireless_version, d.status, d.rssi,d.is_shadow,d.is_simulate ,d.location_way,d.things_model_value, " +
                    "d.network_address, d.network_ip, d.longitude, d.latitude, d.active_time, d.create_time, d.update_time, " +
                    "d.img_url,d.summary,d.remark,p.guid, p.transport, p.protocol_code " +
                    "from iot_device d\n" +
                    "left join iot_product p on p.product_id=d.product_id " +
                    "${ew.getCustomSqlSegment}"
    })
    List<Device> selectAll(@Param(Constants.WRAPPER) Wrapper<Device> queryWrapper);

    @Select({
            "select itmt.template_id, itmt.template_name, itmt.tenant_id, itmt.tenant_name, itmt.identifier, itmt.type, itmt.datatype, itmt.specs, itmt.is_sys, itmt.is_readonly, itmt.is_chart,itmt.is_share_perm, itmt.is_history, itmt.formula, itmt.remark, itmt.model_order " +
                    "from iot_things_model_template itmt " +
                    "${ew.getCustomSqlSegment}"
    })
    List<Device> selectThingsModelList(@Param(Constants.WRAPPER) Wrapper<Device> queryWrapper);

    @Select({
            "select d.device_id, d.device_name, d.product_id, p.product_name,p.device_type, d.tenant_id, d.tenant_name, " +
            "d.serial_number, d.firmware_version,d.wireless_version, d.status, d.rssi,d.is_shadow,d.is_simulate ,d.location_way,d.things_model_value, " +
            "d.network_address, d.network_ip, d.longitude, d.latitude, d.active_time, d.create_time, d.update_time, " +
            "d.img_url,d.summary,d.remark,p.guid, p.transport, p.protocol_code " +
            "from iot_device d\n" +
            "left join iot_product p on p.product_id=d.product_id " +
            "${ew.getCustomSqlSegment}"
    })
    List<Device> selectDeviceList(@Param(Constants.WRAPPER) Wrapper<Device> queryWrapper);


}
