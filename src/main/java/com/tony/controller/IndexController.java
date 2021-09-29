package com.tony.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/rest", produces = "application/json;charset=UTF-8")
public class IndexController {

  @GetMapping("/index")
  public String index() {
    return "index";
  }

}
