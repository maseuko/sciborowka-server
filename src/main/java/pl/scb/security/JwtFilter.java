package pl.scb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.scb.models.AdminModel;
import pl.scb.records.Authority;
import pl.scb.repo.AdminRepo;
import pl.scb.services.AuthDetails;
import pl.scb.utils.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthDetails authDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtAuth = request.getHeader("auth");
        final String jwtRefresh = request.getHeader("refresh");

        if(jwtAuth == null || jwtRefresh == null){
            filterChain.doFilter(request, response);
        }else{
            if(!this.jwtUtil.checkForTokenValidity(jwtAuth) ||
               !this.jwtUtil.checkForTokenValidity(jwtRefresh) ||
               !this.jwtUtil.checkForTokensKeyPairExistence(jwtAuth, jwtRefresh)){
                filterChain.doFilter(request, response);
            }
            long UID = this.jwtUtil.getUserIdFromToken(jwtAuth);
            UserDetails details;
            Optional<AdminModel> admin = this.adminRepo.findById(UID);
            if(admin.isEmpty()){
                filterChain.doFilter(request, response);
            }else{
                 details = admin.get().loadUserByUsername(admin.get().getLogin());
                 UsernamePasswordAuthenticationToken authenticationToken =
                         new UsernamePasswordAuthenticationToken(
                                 details.getUsername(),
                                 details.getPassword(),
                                 authDetails.getAuthorities(admin.get())
                         );
                 authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }
    }
}
