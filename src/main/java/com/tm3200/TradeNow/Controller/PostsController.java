package com.tm3200.TradeNow.Controller;

import com.tm3200.TradeNow.Model.DTO.ModerationDTO;
import com.tm3200.TradeNow.Model.DTO.PostsDTO;
import com.tm3200.TradeNow.Model.Enum.PublicationType;
import com.tm3200.TradeNow.Model.Posts;
import com.tm3200.TradeNow.Model.PostsEntitys.Category;
import com.tm3200.TradeNow.Model.PostsEntitys.Zone;
import com.tm3200.TradeNow.Repository.CategoryJpaRepository;
import com.tm3200.TradeNow.Repository.ZoneJpaRepository;
import com.tm3200.TradeNow.Service.PostsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin(origins = "*")
public class PostsController
{
    @Autowired
    private PostsService postsService;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ZoneJpaRepository zoneJpaRepository;


    @CrossOrigin(origins = "*") // Importante para permitir la conexión desde el frontend
    @GetMapping("/categorias")
    public List<Category> getCategorias() {
        return categoryJpaRepository.findAll();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/zonas")
    public List<Zone> getZonas() {
        return zoneJpaRepository.findAll();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll()
    {
        List<Posts> posts = postsService.findAll();

        if (posts.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(posts);
    }//Fin del metodo

    @GetMapping("/{id}")
    public ResponseEntity<Posts> getPost(@PathVariable Integer id)
    {
        Posts postId = postsService.getPost(id);

        if (postId == null)
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(postId);
    }//Fin del metodo

    @PostMapping("/new")
    public ResponseEntity<?> addPost(@Valid @RequestBody PostsDTO dto, BindingResult result)//Valida si se cumple, BingindResult valida los errores
    {
        if (result.hasErrors())
        {
            List<String> errors = new ArrayList<>();

            for (ObjectError error: result.getAllErrors())
            {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        if (postsService.addPost(dto) == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos inválidos para el tipo de publicación");
        }
        return ResponseEntity.ok("Publicación exitosa");
    }//Fin del metodo

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Integer id, @Valid @RequestBody PostsDTO dto, BindingResult result)
    {
        if (result.hasErrors())
        {
            List<String> errors = new ArrayList<>();

            for (ObjectError error : result.getAllErrors())
            {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        if (postsService.updatePost(id, dto) == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Publicación no encontrada o datos inválidos");
        }

        return ResponseEntity.ok("Publicación actualizada exitosamente");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id,
                                    @RequestParam Integer userId) {

        if (postsService.deletePost(id, userId)) {
            return ResponseEntity.ok("Publicación eliminada exitosamente");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("No puedes eliminar esta publicación.");
    }

    @GetMapping("/pendientes")
    public ResponseEntity<?> getPendingPosts(@RequestParam Integer moderatorId)
    {
        List<Posts> posts = postsService.findPendingPosts(moderatorId);

        if (posts == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para ver publicaciones pendientes");
        }

        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(posts);
    }//Fin del metodo

    @GetMapping("/mias")
    public ResponseEntity<?> getMyPosts(@RequestParam Integer userId)
    {
        List<Posts> posts = postsService.findMyPosts(userId);

        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(posts);
    }//Fin del metodo

    @GetMapping("/filter")
    public ResponseEntity<?> filterPosts(
            @RequestParam(required = false) Integer categoria,
            @RequestParam(required = false) Integer zona,
            @RequestParam(required = false) PublicationType tipo) {

        List<Posts> posts = postsService.filterPosts(categoria, zona, tipo);

        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}/moderar")
    public ResponseEntity<?> moderatePost(@PathVariable Integer id, @Valid @RequestBody ModerationDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        Posts post = postsService.moderatePost(id, dto);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo moderar la publicación. Verifica permisos o estado de la publicación.");
        }

        return ResponseEntity.ok("Publicación " + dto.getStatus() + " exitosamente");
    }//Fin del metodo
}
