package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.DTO.PostsDTO;
import com.tm3200.TradeNow.Model.Enum.PublicationStatus;
import com.tm3200.TradeNow.Model.Enum.PublicationType;
import com.tm3200.TradeNow.Model.Posts;
import com.tm3200.TradeNow.Model.PostsEntitys.Category;
import com.tm3200.TradeNow.Model.PostsEntitys.Zone;
import com.tm3200.TradeNow.Model.User;
import com.tm3200.TradeNow.Repository.CategoryJpaRepository;
import com.tm3200.TradeNow.Repository.PostsJpaRepository;
import com.tm3200.TradeNow.Repository.UserJpaRepository;
import com.tm3200.TradeNow.Repository.ZoneJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService
{
    @Autowired
    private PostsJpaRepository postsJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ZoneJpaRepository zoneJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    //Metodo que lista todas las publicaciones
    public List<Posts> findAll()
    {
        return postsJpaRepository.findAll();
    }//Fin del metodo

    //Metodo que obtiene una publicación especifica
    public Posts getPost(Integer id)
    {
        return postsJpaRepository.getById(id);
    }//Fin del metodo

    //Metodo que crea una nueva publicación
    public Posts addPost(PostsDTO dto) {
        // 1. Validaciones básicas de tipo
        // Cambia esto en addPost:
        if (dto.getType() == PublicationType.OFFER && dto.getEstimatedValue() == null) {
            // Solo rechaza si es estrictamente NULL, permite el 0 si el usuario no puso valor
            return null;
        }
        if (dto.getType() == PublicationType.SEARCH && (dto.getExchangeFor() == null || dto.getExchangeFor().isBlank())) {
            return null;
        }

        // 2. Buscar entidades relacionadas primero (evita errores de nulos)
        User user = userJpaRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            System.out.println("DEBUG: Usuario no encontrado: " + dto.getUserId());
            return null;
        }

        Category category = categoryJpaRepository.findById(dto.getCategoryId()).orElse(null);
        if (category == null) {
            System.out.println("DEBUG: Categoría no encontrada: " + dto.getCategoryId());
            return null;
        }

        Zone zone = zoneJpaRepository.findById(dto.getZoneId()).orElse(null);
        if (zone == null) {
            System.out.println("DEBUG: Zona no encontrada: " + dto.getZoneId());
            return null;
        }

        // 3. Crear y poblar el objeto
        Posts posts = new Posts();
        posts.setType(dto.getType());
        posts.setTitle(dto.getTitle());
        posts.setDescription(dto.getDescription());
        posts.setImageUrl(dto.getImageUrl());
        posts.setStatus(PublicationStatus.PENDING);

        // Conversión segura de Double a BigDecimal
        posts.setEstimatedValue(dto.getEstimatedValue() != null ? BigDecimal.valueOf(dto.getEstimatedValue()) : BigDecimal.ZERO);
        posts.setExchangeFor(dto.getExchangeFor());

        // 4. Asignar entidades y guardar
        posts.setUser(user);
        posts.setCategory(category);
        posts.setZone(zone);

        return postsJpaRepository.save(posts);
    }

    //Editar una publicación propia
//Editar una publicación propia
    public Posts updatePost(Integer id, PostsDTO dto)
    {
        // 1. Buscamos la publicación original
        Posts posts = postsJpaRepository.findById(id).orElse(null);
        if (posts == null) {
            return null;
        }

        // 2. Buscamos las entidades relacionadas (User, Category, Zone) PRIMERO
        User user = userJpaRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            System.out.println("DEBUG: El usuario con ID " + dto.getUserId() + " NO existe.");
            return null;
        }

        Category category = categoryJpaRepository.findById(dto.getCategoryId()).orElse(null);
        if (category == null) {
            System.out.println("DEBUG: La categoría con ID " + dto.getCategoryId() + " NO existe.");
            return null;
        }

        Zone zone = zoneJpaRepository.findById(dto.getZoneId()).orElse(null);
        if (zone == null) {
            System.out.println("DEBUG: La zona con ID " + dto.getZoneId() + " NO existe.");
            return null;
        }

        // 3. Actualizamos los campos
        posts.setType(dto.getType());
        posts.setTitle(dto.getTitle());
        posts.setDescription(dto.getDescription());

        // Manejo de BigDecimal para el valor estimado
        if (dto.getEstimatedValue() != null) {
            posts.setEstimatedValue(BigDecimal.valueOf(dto.getEstimatedValue()));
        } else {
            posts.setEstimatedValue(BigDecimal.ZERO);
        }

        posts.setExchangeFor(dto.getExchangeFor());
        posts.setImageUrl(dto.getImageUrl());

        // 4. Asignamos las entidades encontradas
        posts.setUser(user);
        posts.setCategory(category);
        posts.setZone(zone);

        return postsJpaRepository.save(posts);
    }//Fin del metodo

    //Eliminar una publicación por id
    public boolean deletePost(Integer id)
    {
        Optional<Posts> posts = postsJpaRepository.findById(id);

        if (posts.isPresent())
        {
            postsJpaRepository.deleteById(id);
            return true;
        }else
        {
            return false;
        }

    }//Fin del metodo

    //Filtrar por categoría, zona y tipo
    public List<Posts> filterPosts(Integer categoryId, Integer zoneId, PublicationType type)
    {
        return postsJpaRepository.findByCategoryIdAndZoneIdAndType(categoryId, zoneId, type);
    }//Fin del metodo
}
