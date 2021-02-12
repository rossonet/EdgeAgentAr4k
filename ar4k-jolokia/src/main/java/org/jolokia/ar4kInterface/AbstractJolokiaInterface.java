package org.jolokia.ar4kInterface;

import java.io.IOException;

public interface AbstractJolokiaInterface {

	/**
	 * Initialize the backend systems, the log handler and the restrictor. A
	 * subclass can tune this step by overriding
	 * {@link #createRestrictor(Configuration)}} and
	 * {@link #createLogHandler(ServletConfig, boolean)}
	 *
	 * @param pServletConfig servlet configuration
	 */
	void init(ServletConfig pServletConfig) throws ServletException;

	/** {@inheritDoc} */
	void destroy();

	/** {@inheritDoc} */
	void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	/** {@inheritDoc} */
	void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	/**
	 * OPTION requests are treated as CORS preflight requests
	 *
	 * @param req  the original request
	 * @param resp the response the answer are written to
	 */
	void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

}
