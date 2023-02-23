package com.morSun.config;

import com.morSun.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //1. 注入加密器
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationTokenFilter jwtTokenFilter;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    //2.权限，登录，登出等配置
    @Override
    public void configure(HttpSecurity http) throws Exception {
         http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                 .and()
                 .authorizeRequests()
                 .antMatchers("/login").anonymous()
                 .antMatchers("/logout").authenticated()
                 .antMatchers(HttpMethod.POST,"/comment").authenticated() // 评论需要登录才行
                 .antMatchers("/user/userinfo").authenticated()
                 .anyRequest().permitAll(); //前台项目，大部分是不需要登录访问的（当然你自己想设置也行）
        //配置认证过滤器
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置授权认证异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        http.logout().disable();
    }

    //todo:3.暴露用于用户认证的对象
        // WebSecurityConfigurerAdapter源码中说，如果想要将AuthenticateManager暴露出去，就将authenticationManagerBean注入
         //注意不是authenticationManager，这个方法是用来获取authenticationManager使用的
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
