package dev.gym.workloadservice.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LogFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String transactionId = UUID.randomUUID().toString();
        MDC.put("transactionId", transactionId);

        LOGGER.info("Starting Transaction for req {} : URL= {} : METHOD= {}", transactionId, httpRequest.getRequestURL(), httpRequest.getMethod());

        // we must make sure the request continues to be processed
        chain.doFilter(request, response);

        LOGGER.info(
                "Finishing Transaction for req {} : STATUS= {}",
                transactionId,
                httpResponse.getStatus()
        );

        MDC.clear();
    }
}
