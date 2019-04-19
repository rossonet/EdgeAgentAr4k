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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.shell.Input;
import org.springframework.shell.Shell;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.config.WebFluxConfigurer;

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
public class TerminalWebController {

  // @Autowired
  // private Anima anima;

  @Autowired
  private Shell shell;

  // @Autowired
  // private TemplateEngine templateEngine;

  @Value("${logging.file}")
  private String targetLogFile;

  /*
   * @RequestMapping("/ar4k/terminal.vue") public Mono<String>
   * ar4kTerminalJs(Authentication authentication, Model model, ServerHttpResponse
   * response) { Context ctx = new Context(); ctx.setVariable("selectedMenu",
   * "terminal"); if (authentication != null) { ctx.setVariable("user",
   * authentication.getName()); ctx.setVariable("roles",
   * authentication.getAuthorities()); } ctx.setVariable("logo",
   * anima.getLogoUrl()); model.addAttribute("template",
   * templateEngine.process("terminal.html", ctx));
   * response.getHeaders().add(HttpHeaders.CONTENT_TYPE,
   * "application/javascript; charset=utf-8"); return
   * Mono.just("terminal.vue.js"); }
   */

  @RequestMapping(value = "/ar4k/cmd", method = RequestMethod.POST)
  @ResponseBody
  public Mono<String> ar4kCmd(@RequestBody String payload) {
    String risultato = String.valueOf(shell.evaluate(new Input() {
      @Override
      public String rawText() {
        return payload;
      }
    }));
    System.out.println(payload);
    return Mono.just(risultato);
  }

}
