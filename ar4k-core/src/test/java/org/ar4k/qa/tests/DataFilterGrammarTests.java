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
package org.ar4k.qa.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.core.data.DataChannelFilter;
import org.ar4k.agent.core.data.IDataChannelFilter.Label;
import org.ar4k.agent.core.data.IDataChannelFilter.Operator;
import org.ar4k.agent.helper.StringUtils;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.ar4k.grammar.DataFilterLexer;
import org.ar4k.grammar.DataFilterParser;
import org.jline.builtins.Commands;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, EdgeAgentCore.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class,
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DataFilterGrammarTests {

	@Autowired
	EdgeAgentCore edgeAgentCore;

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(edgeAgentCore.getState());
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void baseCheckFilterError() throws InterruptedException, IOException {
		String filterString = "AND	NAME_SPACE IN OR('que*11(sto', 'qu__e-llo') OR SERVICE_CLASS  IN   AND( 'classe.1','classe.2'   ,'classe3')";
		CharStream charStream = (CharStream) new ANTLRInputStream(filterString);
		DataFilterLexer lexer = new DataFilterLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DataFilterParser parser = new DataFilterParser(tokens);
		int error = parser.getNumberOfSyntaxErrors();
		System.out.println(error);
		assertEquals(0, error);
		ParseTree tree = parser.start();
		System.out.println("tree as string tree -> " + tree.toStringTree());
		System.out.println("tree as text -> " + tree.getText());
		assertEquals(
				"([] ([8] ([29 8] AND NAME_SPACE IN ([27 29 8] OR ( 'que*11(sto' , 'qu__e-llo' ))) ([30 8] OR SERVICE_CLASS IN ([27 30 8] AND ( 'classe.1' , 'classe.2' , 'classe3' )))))",
				tree.toStringTree());
	}

	@Test
	public void baseCheckFilterErrorLowerUpper() throws InterruptedException, IOException {
		String filterString = "AnD	name_SPACE In OR('que*11(sto', 'qu__e-llo') OR SERVICE_class  IN  AND( 'classe.1','classe.2'   ,'classe3')";
		CharStream charStream = (CharStream) new ANTLRInputStream(filterString);
		DataFilterLexer lexer = new DataFilterLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DataFilterParser parser = new DataFilterParser(tokens);
		int error = parser.getNumberOfSyntaxErrors();
		System.out.println(error);
		assertEquals(0, error);
		ParseTree tree = parser.start();
		System.out.println("tree as string tree -> " + tree.toStringTree());
		System.out.println("tree as text -> " + tree.getText());
		assertEquals(
				"([] ([8] ([29 8] AnD name_SPACE In ([27 29 8] OR ( 'que*11(sto' , 'qu__e-llo' ))) ([30 8] OR SERVICE_class IN ([27 30 8] AND ( 'classe.1' , 'classe.2' , 'classe3' )))))",
				tree.toStringTree());
	}

	@Test
	public void baseCheckFilterParser() throws InterruptedException, IOException {
		String filterString = "AND NAME_SPACE IN OR('questo', 'quello') OR SERVICE_CLASS IN AND('classe1','classe2'   ,'classe3')";
		CharStream charStream = (CharStream) new ANTLRInputStream(filterString);
		DataFilterLexer lexer = new DataFilterLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DataFilterParser parser = new DataFilterParser(tokens);
		int error = parser.getNumberOfSyntaxErrors();
		assertEquals(0, error);
		ParseTree tree = parser.start();
		System.out.println("tree as string tree -> " + tree.toStringTree());
		System.out.println("tree as text -> " + tree.getText());
		assertEquals("ANDNAME_SPACEINOR('questo','quello')ORSERVICE_CLASSINAND('classe1','classe2','classe3')",
				tree.getText());
	}

	@Test
	public void baseCheckListener() throws InterruptedException, IOException {
		String filterString = "AND NAME_SPACE IN OR('questo', 'quello') OR SERVICE_CLASS IN AND('classe1','classe2'   ,'classe3')";
		CharStream charStream = (CharStream) new ANTLRInputStream(filterString);
		DataFilterLexer lexer = new DataFilterLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DataFilterParser parser = new DataFilterParser(tokens);
		int error = parser.getNumberOfSyntaxErrors();
		assertEquals(0, error);
		ParseTree tree = parser.start();
		System.out.println("children count -> " + tree.getChildCount());
		assertEquals(1, tree.getChildCount());
		System.out.println("text first children -> " + tree.getChild(0).getText());
		assertEquals("ANDNAME_SPACEINOR('questo','quello')ORSERVICE_CLASSINAND('classe1','classe2','classe3')",
				tree.getChild(0).getText());
		System.out.println("children count of first child -> " + tree.getChild(0).getChildCount());
		assertEquals(2, tree.getChild(0).getChildCount());
		System.out.println("first line -> " + tree.getChild(0).getChild(0).getText());
		assertEquals("ANDNAME_SPACEINOR('questo','quello')", tree.getChild(0).getChild(0).getText());
		System.out.println("second line -> " + tree.getChild(0).getChild(1).getText());
		assertEquals("ORSERVICE_CLASSINAND('classe1','classe2','classe3')", tree.getChild(0).getChild(1).getText());
		System.out.println("second line global operator -> " + tree.getChild(0).getChild(1).getChild(0).getText());
		System.out.println("second line global label -> " + tree.getChild(0).getChild(1).getChild(1).getText());
		System.out.println("second line filter array -> " + tree.getChild(0).getChild(1).getChild(3).getText());
		System.out.println("second line filter array operator -> "
				+ tree.getChild(0).getChild(1).getChild(3).getChild(0).getText());
		System.out.println(
				"second line filter array data 1 -> " + tree.getChild(0).getChild(1).getChild(3).getChild(2).getText());
		System.out.println(
				"second line filter array data 2 -> " + tree.getChild(0).getChild(1).getChild(3).getChild(4).getText());
		System.out.println(
				"second line filter array data 3 -> " + tree.getChild(0).getChild(1).getChild(3).getChild(6).getText());
		System.out.println("tree as string tree -> " + tree.toStringTree());
		System.out.println("tree as text -> " + tree.getText());
		assertEquals("ANDNAME_SPACEINOR('questo','quello')ORSERVICE_CLASSINAND('classe1','classe2','classe3')",
				tree.getText());
	}

	@Test
	public void generateFilterQuery() throws IOException {
		String filterString = "AnD NAME_SpACE IN OR('questo', 'quello') OR			service_CLASS In and('classe1','classe2'   ,'classe3')\nOR_NOT   SERVICE_name   in or('test-name')";
		DataChannelFilter dcf = StringUtils.dataChannelFilterFromString(filterString);
		System.out.println("filter class -> " + dcf.toString());
		assertEquals(3, dcf.getFilters().size());
		// check 1
		assertEquals(Operator.AND, dcf.getFilters().get(0).getFilterGlobalOperator());
		assertEquals(Operator.OR, dcf.getFilters().get(0).getFilterOperator());
		assertEquals(Label.NAME_SPACE, dcf.getFilters().get(0).getFilterLabel());
		assertEquals("questo", dcf.getFilters().get(0).getFilterValues().get(0));
		assertEquals("quello", dcf.getFilters().get(0).getFilterValues().get(1));
		// check 2
		assertEquals(Operator.OR, dcf.getFilters().get(1).getFilterGlobalOperator());
		assertEquals(Operator.AND, dcf.getFilters().get(1).getFilterOperator());
		assertEquals(Label.SERVICE_CLASS, dcf.getFilters().get(1).getFilterLabel());
		assertEquals("classe1", dcf.getFilters().get(1).getFilterValues().get(0));
		assertEquals("classe2", dcf.getFilters().get(1).getFilterValues().get(1));
		assertEquals("classe3", dcf.getFilters().get(1).getFilterValues().get(2));
		// check 3
		assertEquals(Operator.OR_NOT, dcf.getFilters().get(2).getFilterGlobalOperator());
		assertEquals(Operator.OR, dcf.getFilters().get(2).getFilterOperator());
		assertEquals(Label.SERVICE_NAME, dcf.getFilters().get(2).getFilterLabel());
		assertEquals("test-name", dcf.getFilters().get(2).getFilterValues().get(0));
	}

}
