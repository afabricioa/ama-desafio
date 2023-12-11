package br.com.fabricio.ama.amadesafio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.fabricio.ama.amadesafio.models.Usuario;
import br.com.fabricio.ama.amadesafio.repositories.IUsuarioRepositorio;

@Component
public class InicializacaoDados implements CommandLineRunner{

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Override
    public void run(String... args) throws Exception {

        if(usuarioRepositorio.count() == 0){
            System.out.println("Inicializando dados");
            String adminPasswordEncrypted = new BCryptPasswordEncoder().encode("admin");
            Usuario admin = new Usuario("admin", "Administrador", adminPasswordEncrypted, true);
            
            String estoquistaPasswordEncrypted = new BCryptPasswordEncoder().encode("estoquita");
            Usuario estoquista = new Usuario("estoquista", "Estoquista", estoquistaPasswordEncrypted, false);

            usuarioRepositorio.save(admin);
            usuarioRepositorio.save(estoquista);
        }
    }
    
}
