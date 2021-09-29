package com.tony;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootApplication
public class ActivitiApplication {

  @Bean
  public UserDetailsService myUserDetailsService() {
    InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
    String[][] usersGroupsAndRoles = {
        { "system", "system", "ROLE_ACTIVITI_USER" },
        { "admin", "admin", "ROLE_ACTIVITI_ADMIN" }, };
    for (String[] user : usersGroupsAndRoles) {
      List<String> authoritiesStrings = Arrays
          .asList(Arrays.copyOfRange(user, 2, user.length));
      inMemoryUserDetailsManager
          .createUser(new User(user[0], passwordEncoder().encode(user[1]),
              authoritiesStrings.stream()
                  .map(s -> new SimpleGrantedAuthority(s))
                  .collect(Collectors.toList())));
    }
    return inMemoryUserDetailsManager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(
        ActivitiApplication.class);
    springApplication.run(args);
  }

}
