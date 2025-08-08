package org.scoula.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@Api(tags = "Home API")
public class HomeController {
    @GetMapping("/")
    @ApiOperation(value = "Home", notes = "Returns the home page.")
    public String home() {
        log.info("================> HomController /");
        return "index"; // View 이름
    }
}
