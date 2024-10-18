package net.sfkao.majimabot.usuario.application.incrementMinas;

import net.sfkao.majimabot.exceptions.UsuarioNotFoundException;
import net.sfkao.majimabot.usuario.domain.Usuario;
import net.sfkao.majimabot.usuario.domain.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioIncrementMinasImpl implements UsuarioIncrementMinas {


    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public void incrementMinas(long id, int cantidad) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        Usuario usuario = usuarioOptional.orElse(new Usuario().setId(id));

        usuario.setMinasExplotadas(usuario.getMinasExplotadas()+cantidad);
        usuarioRepository.save(usuario);

    }
}
