/**
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
package org.ar4k.agent.core.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.ServiceComponent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.boot.actuate.logging.LoggersEndpoint.LoggerLevels;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Mono;

/*
 * @author Andrea Ambrosini
 * 
 *         Controller web per interfaccia demo Agente Ar4k
 *
 */
@Controller
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
@ConditionalOnProperty(name = "ar4k.web", havingValue = "true")
public class DashboardWebController {

  @Autowired
  private Anima anima;

  @Autowired
  private Environment env;

  // @Autowired
  // private Shell shell;

  @Value("${logging.file}")
  private String targetLogFile;

  @Autowired
  private List<? extends RequestMappingInfoHandlerMapping> listRequestMapping;

  @Autowired
  private List<? extends MeterRegistry> listMetrics;

  @Autowired
  private List<? extends LoggersEndpoint> loggersEndpoint;

  @Autowired
  private TemplateEngine templateEngine;

  @RequestMapping("/ar4k/routes.js")
  public Mono<String> ar4kRoutesJs(Authentication authentication, Model model, ServerHttpResponse response) {
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/javascript; charset=utf-8");
    return Mono.just("routes.js");
  }

  @GetMapping("/")
  @PostMapping("/")
  public Mono<Void> ar4kAppHome(ServerWebExchange exchange) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.SEE_OTHER);
    response.getHeaders().add(HttpHeaders.LOCATION, "/static");
    return response.setComplete();
  }

  @RequestMapping("/ar4k")
  public Mono<String> ar4kIndex(Authentication authentication, Model model) {
    model.addAttribute("selectedMenu", "beans");
    model.addAttribute("logo", anima.getLogoUrl());
    model.addAttribute("user", authentication.getName());
    model.addAttribute("roles", authentication.getAuthorities());
    return Mono.just("index");
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(path = "/ar4k/dashtable/{table}")
  public Mono<String> ar4kTerminalJs(Authentication authentication, Model model, ServerHttpResponse response,
      @PathVariable("table") String table) {
    String targetPage = "";
    Context ctx = new Context();
    switch (table) {
    case "env":
      ctx.setVariable("properties", getProperties());
      targetPage = "tableEnv.html";
      break;
    case "keystores":
      ctx.setVariable("keys", anima.getKeyStores());
      targetPage = "tableKeyStores.html";
      break;
    case "conf":
      ctx.setVariable("configs", anima.getConfigs());
      targetPage = "tableConf.html";
      break;
    case "logger":
      Map<String, LoggerLevels> loggers = new HashMap<String, LoggerLevels>();
      for (LoggersEndpoint log : loggersEndpoint) {
        for (String linea : ((Map<String, LoggerLevels>) log.loggers().get("loggers")).keySet()) {
          loggers.put(linea, ((Map<String, LoggerLevels>) log.loggers().get("loggers")).get(linea));
        }
      }
      ctx.setVariable("loggers", loggers);
      targetPage = "tableLogger.html";
      break;
    case "web":
      List<RequestMappingInfo> map = new ArrayList<RequestMappingInfo>();
      for (RequestMappingInfoHandlerMapping rm : listRequestMapping) {
        for (RequestMappingInfo a : rm.getHandlerMethods().keySet()) {
          map.add(a);
        }
      }
      ctx.setVariable("mappings", map);
      targetPage = "tableWeb.html";
      break;
    case "metrics":
      List<Meter> meters = new ArrayList<Meter>();
      for (MeterRegistry mr : listMetrics) {
        for (Meter m : mr.getMeters()) {
          meters.add(m);
        }
      }
      ctx.setVariable("meters", meters);
      targetPage = "tableMetrics.html";
      break;
    case "blockchain":
      targetPage = "tableBlockchain.html";
      break;
    default:
      throw new ParameterException("table not found...");
    }
    model.addAttribute("template", templateEngine.process(targetPage, ctx));
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/javascript; charset=utf-8");
    return Mono.just("table.js");
  }

  @RequestMapping("/ar4k/dashboard.js")
  public Mono<String> ar4kDashboardJs(Authentication authentication, Model model, ServerHttpResponse response) {
    Context ctx = new Context();
    ctx.setVariable("selectedMenu", "dashboard");
    if (authentication != null) {
      ctx.setVariable("user", authentication.getName());
      ctx.setVariable("roles", authentication.getAuthorities());
    }
    ctx.setVariable("logo", anima.getLogoUrl());
    model.addAttribute("template", templateEngine.process("dashboard.html", ctx));
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/javascript; charset=utf-8");
    return Mono.just("dashboard.js");
  }

  @SuppressWarnings("unchecked")
  @RequestMapping("/ar4k/data/base")
  @ResponseBody
  public Mono<Map<String, Object>> ar4kDataStart(Authentication authentication) {
    // model.addAttribute("selectedMenu", "dashboard");
    Map<String, Object> risposta = new HashMap<String, Object>();
    risposta.put("user", authentication.getName());
    risposta.put("roles", authentication.getAuthorities());
    risposta.put("properties", getProperties());
    risposta.put("keys", anima.getKeyStores());
    risposta.put("configs", anima.getConfigs());
    risposta.put("logo", anima.getLogoUrl());
    Map<String, LoggerLevels> loggers = new HashMap<String, LoggerLevels>();
    for (LoggersEndpoint log : loggersEndpoint) {
      for (String linea : ((Map<String, LoggerLevels>) log.loggers().get("loggers")).keySet()) {
        loggers.put(linea, ((Map<String, LoggerLevels>) log.loggers().get("loggers")).get(linea));
      }
    }
    risposta.put("loggers", loggers);
    List<RequestMappingInfo> map = new ArrayList<RequestMappingInfo>();
    for (RequestMappingInfoHandlerMapping rm : listRequestMapping) {
      for (RequestMappingInfo a : rm.getHandlerMethods().keySet()) {
        map.add(a);
      }
    }
    List<Meter> meters = new ArrayList<Meter>();
    for (MeterRegistry mr : listMetrics) {
      for (Meter m : mr.getMeters()) {
        meters.add(m);
      }
    }
    risposta.put("mappings", map);
    risposta.put("meters", meters);
    return Mono.just(risposta);
  }

  @RequestMapping("/ar4k/swagger")
  public Mono<String> ar4kSwagger(Authentication authentication, Model model) {
    model.addAttribute("selectedMenu", "swagger");
    model.addAttribute("logo", anima.getLogoUrl());
    model.addAttribute("user", authentication.getName());
    model.addAttribute("roles", authentication.getAuthorities());
    return Mono.just("swagger");
  }

  @RequestMapping("/ar4k/beans")
  public Mono<String> ar4kBeans(Authentication authentication, Model model) {
    model.addAttribute("selectedMenu", "beans");
    model.addAttribute("logo", anima.getLogoUrl());
    model.addAttribute("user", authentication.getName());
    model.addAttribute("roles", authentication.getAuthorities());
    return Mono.just("beans");
  }

  @SuppressWarnings("unchecked")
  @RequestMapping("/ar4k/status")
  @ResponseBody
  public Mono<JSONObject> ar4kStatus() {
    JSONObject json = new JSONObject();
    json.put("run-level", (String) anima.getState().name());
    JSONArray runtimeServices = new JSONArray();
    JSONObject runtimeConfig = new JSONObject();
    for (ServiceComponent st : anima.getServices()) {
      JSONObject sjt = new JSONObject();
      sjt.put("status", st.status());
      sjt.put("status", st.getConfiguration().getName());
      sjt.put("idProcess", st.getConfiguration().getUniqueId());
      sjt.put("description", st.getConfiguration().getDescription());
      sjt.put("tags", st.getConfiguration().getTags());
      sjt.put("type", st.getClass().getName());
      runtimeServices.add(sjt);
    }
    if (anima.getRuntimeConfig() != null) {
      runtimeConfig.put("name", anima.getRuntimeConfig().name);
      runtimeConfig.put("description", anima.getRuntimeConfig().description);
      runtimeConfig.put("author", anima.getRuntimeConfig().author);
      runtimeConfig.put("creationDate", anima.getRuntimeConfig().creationDate.toDateTime());
      runtimeConfig.put("dataCenter", anima.getRuntimeConfig().dataCenter);
      runtimeConfig.put("licence", anima.getRuntimeConfig().license);
    }
    json.put("run-services", runtimeServices);
    json.put("run-config", runtimeConfig);
    return Mono.just(json);
  }

  @SuppressWarnings("unchecked")
  @RequestMapping("/ar4k/blockchain")
  @ResponseBody
  public Mono<JSONObject> ar4kEthereum() {
    JSONObject json = new JSONObject();
    // TODO: filtrare solo i blockchains
    for (ServiceComponent st : anima.getServices()) {
      JSONObject sjt = new JSONObject();
      sjt.put("configuration", st.getConfiguration());
    }
    return Mono.just(json);
  }

  private Map<String, String> getProperties() {
    Map<String, String> map = new HashMap<String, String>();
    for (Iterator<?> it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext();) {
      PropertySource<?> propertySource = (PropertySource<?>) it.next();
      if (propertySource instanceof MapPropertySource) {
        Map<String, Object> dato = ((MapPropertySource) propertySource).getSource();
        for (String key : dato.keySet()) {
          map.put(key, dato.get(key).toString());
        }
      }
    }
    return map;
  }

  @GetMapping(path = "/ar4k/conf/json/{id}", produces = "application/json")
  @ResponseBody
  public Mono<String> getConfigJson(@PathVariable("id") String id) {
    String found = "";
    for (Ar4kConfig t : anima.getConfigs()) {
      if (t.uniqueId.toString().equals(id)) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        found = gson.toJson(t);
      }
    }
    return Mono.just(found);
  }

  @GetMapping(path = "/ar4k/conf/base64/{id}", produces = "application/octet-stream")
  @ResponseBody
  public Mono<String> getConfigBase64(@PathVariable("id") String id) {
    String found = "";
    for (Ar4kConfig t : anima.getConfigs()) {
      if (t.uniqueId.toString().equals(id)) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
          oos = new ObjectOutputStream(baos);
          oos.writeObject(t);
          oos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        found = Base64.getEncoder().encodeToString(baos.toByteArray());
      }
    }
    return Mono.just(found);
  }

  @GetMapping(path = "/ar4k/getloggerfile", produces = "text/plain")
  public ResponseEntity<Resource> getLogFile() {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    df.setTimeZone(tz);
    String nowAsISO = df.format(new Date());
    Resource resource = Anima.getApplicationContext().getResource("file:" + targetLogFile);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDispositionFormData("attachment", "file:" + nowAsISO + "_" + anima.getState() + ".log");
    return ResponseEntity.ok().cacheControl(CacheControl.noCache()).headers(headers).body(resource);
  }

}
