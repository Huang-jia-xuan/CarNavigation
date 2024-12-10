package edu.jnu.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单登录
                // 指定了跳转到登录页面的请求UR
                .loginPage("/login.html")
                // 对应登录页面form表单的action="/login"
                .loginProcessingUrl("/login")
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/swagger*//**", "/v2/api-docs", "/login.html", "/login.html",
                        "/css/**").permitAll()
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
                .and().csrf().disable();
    }
}

