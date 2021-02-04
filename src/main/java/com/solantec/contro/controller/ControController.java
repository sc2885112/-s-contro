package com.solantec.contro.controller;


import com.solantec.contro.ControContext;
import com.solantec.contro.ResourceState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("sContro")
public class ControController {

    @Autowired
    ControContext controContext;

    @GetMapping("default_fallback_path")
    public String fallbak() {
        return "contro tryAcquire failure";
    }

    @GetMapping("getResourceStates")
    public Collection<ResourceState> getResorceStates() {
        return controContext.getResourceStates().values();
    }
}
