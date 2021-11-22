package com.offcn.uauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity //启用springSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .anyRequest()//任何请求都要拦截
                .and()
                .formLogin()//登录界面
                .and()
                .csrf().disable();//跨站攻击防御禁用
    }

    /***
     * 忽略安全拦截的URL
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/oauth/login",
                "/oauth/logout",
                "/user/login");//放行自定义登录地址
    }


    //认证管理器
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    //声明自定义认证对象
//    @Bean(name = "userDetailsService")
//    @Override
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        return this.CreateUserDetailsService();
//    }
//
//    @Override
//    protected UserDetailsService userDetailsService() {
//        //启用自定义认证对象进行认证
//        return   this.CreateUserDetailsService();
//    }
//
//    //自定义认证，声明用户登录账号、密码
//    private UserDetailsService CreateUserDetailsService(){
//        List<UserDetails> users=new ArrayList<>();
//        UserDetails adminUser = User.withUsername("admin").password(passwordEncoder().encode("123")).authorities("ADMIN", "USER").build();
//        UserDetails OneUser = User.withUsername("user1").password(passwordEncoder().encode("123")).authorities("ADMIN", "USER").build();
//        UserDetails TowUser = User.withUsername("user2").password(passwordEncoder().encode("456")).authorities("USER").build();
//        users.add(adminUser);
//        users.add(OneUser);
//        users.add(TowUser);
//        return new InMemoryUserDetailsManager(users);
//    }


    //声明密码加密器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
