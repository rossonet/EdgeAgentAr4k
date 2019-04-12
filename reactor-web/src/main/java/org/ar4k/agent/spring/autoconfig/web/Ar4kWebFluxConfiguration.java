/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.spring.autoconfig.web;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.resource.WebJarsResourceResolver;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;

@Configuration
@EnableWebFlux
public class Ar4kWebFluxConfiguration implements WebFluxConfigurer {

  @Autowired
  private ApplicationContext applicationContext;

  @Bean
  public WebFluxConfigurer corsConfigurer() {
    return new WebFluxConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
      }
    };
  }

  @Bean
  RouterFunction<ServerResponse> staticResourceRouter() {
    return RouterFunctions.resources("/static/**", new ClassPathResource("/static/"));
  }

  @Bean
  RouterFunction<ServerResponse> imagesResourceRouter() {
    return RouterFunctions.resources("/images/**", new ClassPathResource("/images/"));
  }

  @Bean
  RouterFunction<ServerResponse> swaggerResourceRouter() {
    return RouterFunctions.resources("/swagger-ui/**", new ClassPathResource("/swagger/"));
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
        .setCacheControl(CacheControl.maxAge(30L, TimeUnit.MINUTES).cachePublic()).resourceChain(true)
        .addResolver(new WebJarsResourceResolver());
  }

  @Bean
  public SpringWebFluxTemplateEngine templateEngine() {
    SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    return templateEngine;
  }

  @Bean
  public SpringResourceTemplateResolver templateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(this.applicationContext);
    templateResolver.setPrefix("classpath:/templates/");
    templateResolver.setSuffix(".html");
    return templateResolver;
  }

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    ThymeleafReactiveViewResolver thymeleafReactiveViewResolver = new ThymeleafReactiveViewResolver();
    thymeleafReactiveViewResolver.setTemplateEngine(templateEngine());
    thymeleafReactiveViewResolver.setApplicationContext(this.applicationContext);
    registry.viewResolver(thymeleafReactiveViewResolver);
  }

}
