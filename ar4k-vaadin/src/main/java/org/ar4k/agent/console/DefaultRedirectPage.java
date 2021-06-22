package org.ar4k.agent.console;

import org.ar4k.agent.core.Homunculus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultRedirectPage {

	@Autowired
	private Homunculus homunculus;

	@RequestMapping(value = "/")
	public String mainPage() {
		return "redirect:" + homunculus.getStarterProperties().getDefaultWebPage();
	}

}
