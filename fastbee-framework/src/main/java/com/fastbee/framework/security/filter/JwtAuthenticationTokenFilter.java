package com.fastbee.framework.security.filter;

import java.io.IOException;
import java.util.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.HashUtil;
import cn.hutool.json.JSONUtil;
import com.fastbee.common.constant.Constants;
import com.fastbee.common.core.domain.entity.SysDept;
import com.fastbee.common.core.domain.entity.SysRole;
import com.fastbee.common.core.domain.entity.SysUser;
import com.fastbee.common.utils.ServletUtils;
import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fastbee.common.core.domain.model.LoginUser;
import com.fastbee.common.utils.SecurityUtils;
import com.fastbee.common.utils.StringUtils;
import com.fastbee.framework.web.service.TokenService;

/**
 * token过滤器 验证token有效性
 * 
 * @author ruoyi
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        LoginUser loginUser;
        Map<String, String> paramMap = ServletUtils.getHeaders(request);
        if (StringUtils.isNotEmpty(paramMap.get("login-user"))) {
            loginUser = getToken(paramMap);
            tokenService.setLoginUser(loginUser);
        }else {
            loginUser = tokenService.getLoginUser(request);
            if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())){
                tokenService.verifyToken(loginUser);
            }
        }
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication()))
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }

    private LoginUser getToken(Map<String, String> paramMap){
        LoginUser loginUser = new LoginUser();
        //登录对象组装
        Map<String,Object> loginUserMap = JSONUtil.toBean(ServletUtils.urlDecode(paramMap.get("login-user")), Map.class);
        String id = loginUserMap.get("id").toString();
        String expiresTime = loginUserMap.get("expiresTime").toString();
        loginUser.setUserId(Long.valueOf(id));
        loginUser.setExpireTime(Long.valueOf(expiresTime));
        //用户对象组装
        Map<String,String> info = JSONUtil.toBean(JSONUtil.toJsonPrettyStr(loginUserMap.get("info")), Map.class);
        if (info != null) {
            SysUser sysUser = new SysUser();
            sysUser.setLanguage("zh-CN");
            sysUser.setUserId(loginUser.getUserId());
            sysUser.setDeptId(Long.valueOf(info.get("deptId")));
            sysUser.setUserName(info.get("userName"));
            sysUser.setRoles(Collections.singletonList(JSONUtil.toBean("{\"roleId\":1,\"roleName\":\"超级管理员\",\"roleKey\":\"admin\",\"roleSort\":1,\"dataScope\":\"1\",\"menuCheckStrictly\":false,\"deptCheckStrictly\":false,\"status\":\"0\",\"flag\":false,\"params\":{}}\n", SysRole.class)));
            SysDept sysDept = new SysDept();
            sysDept.setDeptId(sysUser.getDeptId());
            sysDept.setDeptUserId(1L);
            sysDept.setUserName(info.get("userName"));
            sysDept.setDeptUserName(info.get("userName"));
            sysUser.setDept(sysDept);
            loginUser.setUser(sysUser);
        }
        String token = paramMap.get("authorization");
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
            loginUser.setToken(token);
            loginUser.setPermissions(CollUtil.newHashSet("*:*:*"));
        }
        return loginUser;
    }

}
