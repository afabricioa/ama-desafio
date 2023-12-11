package br.com.fabricio.ama.amadesafio.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fabricio.ama.amadesafio.repositories.IUsuarioRepositorio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{

    @Autowired
    TokenService tokenService;

    @Autowired
    IUsuarioRepositorio usuarioRepositorio;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);
        System.out.println("token: " + token);
        if(token != null){
            var username = tokenService.validateToken(token);
            System.out.println("username " + username);
            UserDetails usuario = usuarioRepositorio.findByUsername(username);
            System.out.println("usuario: " + usuario);
            var autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        } 
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        System.out.println(request);
        var authHeader = request.getHeader("Authorization");
        System.out.println("header: " + authHeader);
        if(authHeader == null)
            return null;
        
        return authHeader.replace("Bearer ", "");
    }
    
}
