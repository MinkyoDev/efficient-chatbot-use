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

@WebFilter("/auth/*")
public class SesstionCheckFilter extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String absURL = URLUtils.getAbsoluteURL(req);
		String requestURL = req.getRequestURL().toString();

		HttpSession session = req.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user != null) {
			if (requestURL.contains("change-password") || requestURL.contains("logout")) {
				chain.doFilter(request, response);
				return;
			}

			res.sendRedirect(absURL + "/chat/chat");
			return;
		}
		chain.doFilter(request, response);
	}
}
