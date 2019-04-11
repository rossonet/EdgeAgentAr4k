package org.ar4k.agent.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class Ar4kAuthenticationManager implements AuthenticationManager {

  @Autowired
  UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails u = userDetailsService.loadUserByUsername(authentication.getName());
    if (u != null && u.getPassword().equals(authentication.getCredentials())) {
      return new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), u.getAuthorities());
    }
    throw new BadCredentialsException("Bad Credentials");
  }

}
