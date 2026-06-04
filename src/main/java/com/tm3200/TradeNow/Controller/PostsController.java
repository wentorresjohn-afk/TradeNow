package com.tm3200.TradeNow.Controller;

import com.tm3200.TradeNow.Model.DTO.PostsDTO;
import com.tm3200.TradeNow.Model.Enum.PublicationType;
import com.tm3200.TradeNow.Model.Posts;
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
public class PostsController
{
    @Autowired
    private PostsService postsService;

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        if (postsService.deletePost(id)) {
            return ResponseEntity.ok("Publicación eliminada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicación no encontrada");
    }

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
}
