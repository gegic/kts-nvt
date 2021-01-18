package rs.ac.uns.ftn.ktsnvt.kultura.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.service.UserService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.TokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    public TokenAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String username;
        String authToken = TokenUtils.getToken(httpServletRequest);
        if (authToken != null) {
            // uzmi username iz tokena
            username = TokenUtils.getUsernameFromToken(authToken);

            if (username != null) {
                // uzmi user-a na osnovu username-a
                try{
                    User user = userService.loadUserByUsername(username);
                    if (TokenUtils.validateToken(authToken, user)) {
                        // kreiraj autentifikaciju
                        Token authentication = new Token(user);
                        authentication.setCredentials(authToken);
                        authentication.setAuthenticated(true);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }catch (UsernameNotFoundException e) {
                    e.printStackTrace();
                }

                // proveri da li je prosledjeni token validan

            }
        }

        // prosledi request dalje u sledeci filter
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
