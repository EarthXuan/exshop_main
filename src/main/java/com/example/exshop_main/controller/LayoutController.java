package com.example.exshop_main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/layout/")
public class LayoutController {

    @RequestMapping("index.do")
    public String index(){
        return "index";
    }
}
