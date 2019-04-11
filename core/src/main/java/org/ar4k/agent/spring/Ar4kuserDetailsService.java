package org.ar4k.agent.spring;

import org.ar4k.agent.core.Anima;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class Ar4kuserDetailsService implements UserDetailsService {

  @Autowired
  Anima anima;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails result = null; // anonymous for spring
    for (Ar4kUserDetails q : anima.getLocalUsers()) {
      if (q.getUsername().equals(username)) {
        result = q;
        break;
      }
    }
    return result;
  }

}
