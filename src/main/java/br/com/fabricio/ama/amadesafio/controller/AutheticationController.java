package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabricio.ama.amadesafio.dtos.AutenticacaoDTO;
import br.com.fabricio.ama.amadesafio.dtos.AutenticacaoResponseDTO;
import br.com.fabricio.ama.amadesafio.models.Usuario;
import br.com.fabricio.ama.amadesafio.security.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AutheticationController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody AutenticacaoDTO data) {
        var usuarioSenha = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        
        try {
            var auth = this.authenticationManager.authenticate(usuarioSenha);
            System.out.println("auth: " + auth);
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    
}
