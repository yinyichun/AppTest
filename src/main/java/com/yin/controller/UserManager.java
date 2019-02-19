package com.yin.controller;


import com.yin.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Api(value = "v1",description = "用户管理系统")
@RestController
@Slf4j
@RequestMapping("v1")
public class UserManager {
    @Autowired
    private SqlSessionTemplate template;

    @ApiOperation(value = "登录接口",httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public boolean login(HttpServletResponse response, @RequestBody User user){
        int i = template.selectOne("login",user);
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        log.info("查看到的结果数是"+i);

        if(i==1){
            log.info("登录用户是："+user.getUserName());
            return true;
        }
        return false;
    }

    @ApiOperation(value = "添加用户接口",httpMethod = "POSY")
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public boolean addUser(HttpServletRequest request,@RequestBody User user){
        boolean x = verifyCookies(request);
        int result =0;
        if(x){
            result = template.insert("addUser",user);
        }
        if(result >0){
            log.info("添加用户数量为：" + result);
            return true;
        }
        return false;
    }
@ApiOperation(value = "获取用户（列表）信息接口",httpMethod = "POST")
@RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request,@RequestBody User user){
        boolean x = verifyCookies(request);
        if(x){
            List<User> users = template.selectList("getUserInfo",user);
            log.info("getUserInfo获取到的用户数是："+users.size());
            return users;
        }else {
            return null;
        }
    }

    @ApiOperation(value = "更新或者删除用户接口",httpMethod = "POST")
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public int updateUser(HttpServletRequest request,@RequestBody User user){
        boolean x = verifyCookies(request);
        int i=0;
        if(x){
            i = template.update("updateUser",user);
        }
        log.info("更新数据条目数为："+i);
        return i;

    }

    private boolean verifyCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            log.info("Cookies为空");
            return  false;
        }
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("login")&&cookie.getValue().equals("true")){
                log.info("cookies 验证通过");
                return true;
            }
        }
        return false;
    }
}
