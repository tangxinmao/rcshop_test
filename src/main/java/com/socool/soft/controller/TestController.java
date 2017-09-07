package com.socool.soft.controller;

import com.socool.soft.bo.RcOrder;
import com.socool.soft.service.IOrderService;
import com.socool.soft.service.IPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private IPushService pushService;
    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/push", method = RequestMethod.GET)
    public String testPush() throws Exception {
        RcOrder order = orderService.findOrderById(1110346721112L);
        pushService.merchantDeliver(order);
        System.out.println("success");
        return "success";
    }

}
