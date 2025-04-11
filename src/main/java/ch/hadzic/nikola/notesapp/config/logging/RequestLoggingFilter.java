package ch.hadzic.nikola.notesapp.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * RequestLoggingFilter is a filter that logs the details of incoming HTTP requests.
 * It extends OncePerRequestFilter to ensure that it is executed once per request.
 * The filter logs the HTTP method, URI, query string, remote address, session ID,
 * content type, and body of the request.
 */
@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);

        String method = wrappedRequest.getMethod();
        String uri = wrappedRequest.getRequestURI();
        String queryString = wrappedRequest.getQueryString();
        String remoteAddr = wrappedRequest.getRemoteAddr();
        String sessionId = wrappedRequest.getRequestedSessionId();
        String contentType = wrappedRequest.getContentType();
        String bodyContent = wrappedRequest.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

        log.info("""
                Request:
                Information:
                  Method       : {}
                  URI          : {}
                  Query        : {}
                  Remote Addr  : {}
                  Session ID   : {}
                  Content-Type : {}
                  Body         : {}
                """, method, uri, queryString, remoteAddr, sessionId, contentType, bodyContent);

        filterChain.doFilter(wrappedRequest, response);
    }

    private static class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

        private final byte[] body;

        public MultiReadHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            body = request.getInputStream().readAllBytes();
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() {
                    return byteArrayInputStream.read();
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }
}
