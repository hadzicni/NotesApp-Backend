package ch.hadzic.nikola.notesapp.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequestWrapper;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);

        String method = wrappedRequest.getMethod();
        String uri = wrappedRequest.getRequestURI();
        String queryString = wrappedRequest.getQueryString();
        String remoteAddr = wrappedRequest.getRemoteAddr();
        String sessionId = wrappedRequest.getRequestedSessionId();
        String contentType = wrappedRequest.getContentType();

        log.info("Request: method={}, uri={}, query={}, remoteAddr={}, sessionId={}, contentType={}",
                method, uri, queryString, remoteAddr, sessionId, contentType);

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
                public void setReadListener(ReadListener readListener) {}

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
