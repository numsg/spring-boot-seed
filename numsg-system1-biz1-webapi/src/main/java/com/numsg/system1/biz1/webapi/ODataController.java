package com.numsg.system1.biz1.webapi;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by numsg on 2017/12/18
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/OdataService.svc/**")
public class ODataController {//extends NumsgODataController {

}
