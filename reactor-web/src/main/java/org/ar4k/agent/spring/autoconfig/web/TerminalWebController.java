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
package org.ar4k.agent.spring.autoconfig.web;

import org.ar4k.agent.core.Anima;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
public class TerminalWebController {

  @Autowired
  private Anima anima;

  @Autowired
  private Shell shell;
  
  @Autowired
  private TemplateEngine templateEngine;

  @Value("${logging.file}")
  private String targetLogFile;

  @RequestMapping("/ar4k/terminal")
  public Mono<String> ar4kUsersConsole(Authentication authentication, Model model) {
    model.addAttribute("selectedMenu", "terminal");
    model.addAttribute("user", authentication.getName());
    model.addAttribute("roles", authentication.getAuthorities());
    model.addAttribute("logo", anima.getLogoUrl());
    return Mono.just("terminal");
  }

  @RequestMapping("/ar4k/terminal.js")
  public Mono<String> ar4kTerminalJs(Authentication authentication, Model model, ServerHttpResponse response) {
    Context ctx = new Context();
    ctx.setVariable("selectedMenu", "terminal");
    if (authentication != null) {
      ctx.setVariable("user", authentication.getName());
      ctx.setVariable("roles", authentication.getAuthorities());
    }
    ctx.setVariable("logo", anima.getLogoUrl());
    model.addAttribute("template", templateEngine.process("terminal.html", ctx));
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/javascript; charset=utf-8");
    return Mono.just("terminal.js");
  }

  @SuppressWarnings("unchecked")
  @RequestMapping("/ar4k/cmd")
  @ResponseBody
  public Mono<JSONObject> ar4kCmd(@RequestBody String payload) {

    String risultato = String.valueOf(shell.evaluate(new Input() {
      @Override
      public String rawText() {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
          json = (JSONObject) parser.parse(payload);
        } catch (org.json.simple.parser.ParseException e) {
          e.printStackTrace();
        }
        String comando = (String) json.get("method");
        String parametri = "";
        for (Object a : ((JSONArray) json.get("params"))) {
          if (a instanceof String) {
            String txt = (String) a;
            parametri += " " + txt;
          }
        }
        return comando + parametri;
      }
    }));
    JSONObject ritorno = new JSONObject();
    JSONParser parserT = new JSONParser();
    JSONObject jsonT = null;
    try {
      jsonT = (JSONObject) parserT.parse(payload);
    } catch (org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    }
    ritorno.put("result", risultato);
    ritorno.put("id", jsonT.get("id"));
    return Mono.just(ritorno);
  }

}
