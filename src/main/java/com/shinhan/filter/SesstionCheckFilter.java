package com.shinhan.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shinhan.domain.dto.UserDTO;
import com.shinhan.utils.URLUtils;

@WebFilter("/*")
public class SesstionCheckFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;

	private final String[] allowedUrls = { "/sign-in", "/sign-up" };

	private boolean isLoginCheckPath(String requestURL) {
		for (String allowedUrl : allowedUrls) {
			if (requestURL.contains(allowedUrl)) {
				return true;
			}
		}
		return false;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String requestURI = req.getRequestURI();
		if (isLoginCheckPath(requestURI)) {
			HttpSession session = req.getSession();
			UserDTO user = (UserDTO) session.getAttribute("user");
			if (user != null) {
				String absURL = URLUtils.getAbsoluteURL(req);
				res.sendRedirect(absURL + "/chat");
				return;
			}
			chain.doFilter(request, response);
			return;
		}
		chain.doFilter(request, response);
	}
}
