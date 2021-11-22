package com.offcn.uauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
@EnableWebSecurity //����springSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.requestMatchers()
               .anyRequest()//�κ�����Ҫ����
               .and()
               .formLogin()//��¼����
               .and()
               .csrf().disable();//��վ������������
    }


    //��֤������
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //�����Զ�����֤����
    @Bean(name = "userDetailsService")
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return this.CreateUserDetailsService();
    }

    @Override
    protected UserDetailsService userDetailsService() {
     //�����Զ�����֤���������֤
     return   this.CreateUserDetailsService();
    }

    //�Զ�����֤�������û���¼�˺š�����
    private UserDetailsService CreateUserDetailsService(){
        List<UserDetails> users=new ArrayList<>();
        UserDetails adminUser = User.withUsername("admin").password(passwordEncoder().encode("123")).authorities("ADMIN", "USER").build();
        UserDetails OneUser = User.withUsername("user1").password(passwordEncoder().encode("123")).authorities("ADMIN", "USER").build();
        UserDetails TowUser = User.withUsername("user2").password(passwordEncoder().encode("456")).authorities("USER").build();
        users.add(adminUser);
        users.add(OneUser);
        users.add(TowUser);
        return new InMemoryUserDetailsManager(users);
    }



    //�������������
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
