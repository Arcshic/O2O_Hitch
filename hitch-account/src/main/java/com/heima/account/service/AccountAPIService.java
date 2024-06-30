package com.heima.account.service;

import com.heima.modules.po.AccountPO;
import com.heima.modules.po.OrderPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "hitch-storage-server", path = "/storage/account", contextId = "account")
public interface AccountAPIService {
    /**
     * 账户注册
     *
     * @param accountPO
     * @return
     */
    @RequestMapping("/register")
    public AccountPO register(@RequestBody AccountPO accountPO);


    /**
     * 账户修改
     *
     * @param record
     */
    @RequestMapping("/update")
    public void update(@RequestBody AccountPO record);

    /**
     * 获取账户信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/getAccountByID/{id}")
    public AccountPO getAccountByID(@PathVariable("id") String id);

    /**
     * 检查登录
     *
     * @param accountPO
     * @return
     */
    @RequestMapping("/checkLogin")
    public AccountPO checkLogin(@RequestBody AccountPO accountPO);

}
