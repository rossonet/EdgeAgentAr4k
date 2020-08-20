package org.ar4k.agent.spring;

import org.ar4k.agent.core.Anima;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class Ar4kuserDetailsService implements ReactiveUserDetailsService {

  @Autowired
  Anima anima;

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    UserDetails result = null; // anonymous for spring
    for (Ar4kUserDetails q : anima.getLocalUsers()) {
      if (q.getUsername().equals(username)) {
        result = q;
        break;
      }
    }
    return Mono.just(result);
  }

}
