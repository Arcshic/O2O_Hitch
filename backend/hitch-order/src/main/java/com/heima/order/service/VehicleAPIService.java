package com.heima.order.service;

import com.heima.modules.po.AuthenticationPO;
import com.heima.modules.po.VehiclePO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "hitch-storage-server", path = "/storage/vehicle", contextId = "vehicle")
public interface VehicleAPIService {


    /**
     * 新增订单
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public VehiclePO add(@RequestBody VehiclePO record);

    @RequestMapping("/update")
    public void update(@RequestBody VehiclePO record);


    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<VehiclePO> selectlist(@RequestBody VehiclePO record);


    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public VehiclePO selectByID(@PathVariable("id") String id);

    /**
     * 根据手机号码查询用户信息
     *
     * @param phone
     * @return
     */
    @RequestMapping("/selectByPhone/{phone}")
    VehiclePO selectByPhone(@PathVariable("phone") String phone);
}
