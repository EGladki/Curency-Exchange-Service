package com.gladkiei.exchanger.filter;


import com.gladkiei.exchanger.exception.AlreadyExistException;
import com.gladkiei.exchanger.exception.BadRequestException;
import com.gladkiei.exchanger.exception.DatabaseAccessException;
import com.gladkiei.exchanger.exception.NotFoundException;
import com.gladkiei.exchanger.utils.JsonUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);
        } catch (BadRequestException e) {
            httpServletResponse.setStatus(SC_BAD_REQUEST);
            JsonUtil.sendJson(httpServletResponse, Map.of("message", e.getMessage()));

        } catch (AlreadyExistException e) {
            httpServletResponse.setStatus(SC_CONFLICT);
            JsonUtil.sendJson(httpServletResponse, Map.of("message", e.getMessage()));

        } catch (NotFoundException e) {
            httpServletResponse.setStatus(SC_NOT_FOUND);
            JsonUtil.sendJson(httpServletResponse, Map.of("message", e.getMessage()));

        } catch (DatabaseAccessException e) {
            httpServletResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
            JsonUtil.sendJson(httpServletResponse, Map.of("message", e.getMessage()));
        }
    }
}
