package org.example.lab05_20206830.controller;

import org.example.lab05_20206830.entity.Usuario;
import org.example.lab05_20206830.entity.Mensaje;
import org.example.lab05_20206830.repository.UsuarioRepository;
import org.example.lab05_20206830.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MensajeRepository mensajeRepository;

    // Página principal: lista de usuarios
    @GetMapping("/")
    public String index(Model model) {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            model.addAttribute("usuarios", usuarios);
        } catch (Exception e) {
            model.addAttribute("usuarios", Collections.emptyList());
            model.addAttribute("error", "Error conectando a la base de datos");
        }
        return "index";
    }

    // Registro de usuario
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro";
        }
        try {
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente!");
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "registro";
        }
    }

    @GetMapping("/enviar-mensaje")
    public String mostrarFormularioMensaje(Model model, @RequestParam(required = false) Integer destinatarioId) {
        model.addAttribute("mensaje", new Mensaje());
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            model.addAttribute("usuarios", usuarios);
            if (destinatarioId != null) {
                Optional<Usuario> destinatario = usuarioRepository.findById(destinatarioId);
                destinatario.ifPresent(u -> model.addAttribute("destinatarioSeleccionado", u));
            }
        } catch (Exception e) {
            model.addAttribute("usuarios", Collections.emptyList());
            model.addAttribute("error", "Error cargando usuarios");
        }
        return "enviar-mensaje";
    }

    @PostMapping("/enviar-mensaje")
    public String procesarEnvioMensaje(
            @RequestParam Integer remitenteId,
            @RequestParam Integer destinatarioId,
            @RequestParam String regaloTipo,
            @RequestParam(required = false) String regaloColor,
            @RequestParam String contenido,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<Usuario> remitenteOpt = usuarioRepository.findById(remitenteId);
            Optional<Usuario> destinatarioOpt = usuarioRepository.findById(destinatarioId);
            if (!remitenteOpt.isPresent() || !destinatarioOpt.isPresent()) {
                model.addAttribute("error", "Usuario no encontrado");
                return "enviar-mensaje";
            }
            if (regaloTipo.equals("carrito") && (regaloColor == null || regaloColor.trim().isEmpty())) {
                model.addAttribute("error", "Debe seleccionar un color para el carrito");
                return "enviar-mensaje";
            }
            if (regaloTipo.equals("flor")) {
                regaloColor = "amarillo";
            }
            // Validación de mensaje
            if (contenido == null || contenido.trim().length() < 20 || contenido.toLowerCase().contains("odio") || contenido.toLowerCase().contains("feo")) {
                model.addAttribute("error", "El mensaje debe tener al menos 20 caracteres y no contener 'odio' ni 'feo'");
                return "enviar-mensaje";
            }
            Mensaje mensaje = new Mensaje();
            mensaje.setRemitente(remitenteOpt.get());
            mensaje.setDestinatario(destinatarioOpt.get());
            mensaje.setRegaloTipo(regaloTipo);
            mensaje.setRegaloColor(regaloColor);
            mensaje.setContenido(contenido);
            mensajeRepository.save(mensaje);
            redirectAttributes.addFlashAttribute("mensaje", "¡Mensaje enviado exitosamente!");
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Error al enviar mensaje: " + e.getMessage());
            return "enviar-mensaje";
        }
    }

    // Ranking de usuarios
    @GetMapping("/ranking")
    public String mostrarRanking(Model model) {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Map<String, Object>> ranking = new ArrayList<>();
            for (Usuario usuario : usuarios) {
                Map<String, Object> usuarioData = new HashMap<>();
                usuarioData.put("usuario", usuario);
                long totalRegalos = mensajeRepository.countTotalRegalosByDestinatario(usuario);
                usuarioData.put("totalRegalos", totalRegalos);
                ranking.add(usuarioData);
            }
            ranking.sort((a, b) -> Long.compare((Long) b.get("totalRegalos"), (Long) a.get("totalRegalos")));
            model.addAttribute("ranking", ranking);
        } catch (Exception e) {
            model.addAttribute("ranking", Collections.emptyList());
            model.addAttribute("error", "Error cargando el ranking");
        }
        return "ranking";
    }

    // Mensajes recibidos
    @GetMapping("/mensajes")
    public String mostrarMensajes(Model model, @RequestParam(required = false) Integer usuarioId) {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            model.addAttribute("usuarios", usuarios);
            if (usuarioId != null) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
                if (usuarioOpt.isPresent()) {
                    List<Mensaje> mensajes = mensajeRepository.findByDestinatarioOrderByFechaEnvioDesc(usuarioOpt.get());
                    long totalMensajes = mensajeRepository.countByDestinatario(usuarioOpt.get());
                    model.addAttribute("mensajes", mensajes);
                    model.addAttribute("totalMensajes", totalMensajes);
                    model.addAttribute("usuarioSeleccionado", usuarioOpt.get());
                } else {
                    model.addAttribute("error", "Usuario no encontrado");
                }
            } else {
                model.addAttribute("mensajes", Collections.emptyList());
                model.addAttribute("totalMensajes", 0);
            }
        } catch (Exception e) {
            model.addAttribute("mensajes", Collections.emptyList());
            model.addAttribute("totalMensajes", 0);
            model.addAttribute("error", "Error cargando mensajes");
        }
        return "mensajes";
    }
}
