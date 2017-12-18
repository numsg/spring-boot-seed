package com.gsafety.xseed.system1.bz1.controller;

import com.gsafety.xseed.system1.common.configs.XseedSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;


/**
 * Created by Administrator on 2017/3/2.
 */
@RestController
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private XseedSettings xseedSettings;

    @RequestMapping("/hello")
    public String index() {
        logger.info("this is test info msg1");
        logger.error("this is test error msg1");
        String msg = messageSource.getMessage("welcome", null, Locale.ENGLISH);

        logger.error("this is test XseedSettings msg:" + xseedSettings.getUrl1());
        return "Greetings from Spring Boot!";
    }
}
