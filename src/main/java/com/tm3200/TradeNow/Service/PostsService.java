package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.DTO.ModerationDTO;
import com.tm3200.TradeNow.Model.DTO.PostsDTO;
import com.tm3200.TradeNow.Model.Enum.PublicationStatus;
import com.tm3200.TradeNow.Model.Enum.PublicationType;
import com.tm3200.TradeNow.Model.Enum.UserType;
import com.tm3200.TradeNow.Model.Posts;
import com.tm3200.TradeNow.Model.PostsEntitys.Category;
import com.tm3200.TradeNow.Model.PostsEntitys.Zone;
import com.tm3200.TradeNow.Model.Proposal;
import com.tm3200.TradeNow.Model.User;
import com.tm3200.TradeNow.Repository.*;
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

    @Autowired
    private ProposalJpaRepository proposalJpaRepository;

    //Metodo que lista todas las publicaciones (solo las aprobadas, visibles al público)
    public List<Posts> findAll()
    {
        return postsJpaRepository.findByStatus(PublicationStatus.APPROVED);
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

            // ESTO IMPRIMIRÁ LO QUE LLEGA DESDE EL FORMULARIO EN LOS LOGS DE RENDER
            System.out.println("DEBUG: DTO Recibido -> Tipo: " + dto.getType() +
                    ", Valor: " + dto.getEstimatedValue() +
                    ", UserID: " + dto.getUserId() +
                    ", CatID: " + dto.getCategoryId() +
                    ", ZonaID: " + dto.getZoneId());

            // ... el resto de tu código

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

    //Eliminar una publicación por id (solo administradores). Tambien elimina las propuestas asociadas
    public boolean deletePost(Integer id, Integer adminId)
    {
        User admin = userJpaRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        if (admin.getUserType() != UserType.ADMINISTRATOR) {
            throw new RuntimeException("Solo los administradores pueden eliminar publicaciones");
        }

        Optional<Posts> posts = postsJpaRepository.findById(id);

        if (posts.isPresent())
        {
            Posts post = posts.get();

            //Borramos primero las propuestas donde esta publicacion es la publicacion objetivo
            List<Proposal> proposalsAsTarget = proposalJpaRepository.findByTargetPublicationId(post);
            proposalJpaRepository.deleteAll(proposalsAsTarget);

            //Borramos tambien las propuestas donde esta publicacion fue la ofrecida a cambio
            List<Proposal> proposalsAsOffered = proposalJpaRepository.findByOfferedPublicationId(post);
            proposalJpaRepository.deleteAll(proposalsAsOffered);

            postsJpaRepository.deleteById(id);
            return true;
        }else
        {
            return false;
        }

    }//Fin del metodo

    //Filtrar por categoría, zona y tipo (solo publicaciones aprobadas)
    public List<Posts> filterPosts(Integer categoryId, Integer zoneId, PublicationType type)
    {
        return postsJpaRepository.findByCategoryIdAndZoneIdAndTypeAndStatus(categoryId, zoneId, type, PublicationStatus.APPROVED);
    }//Fin del metodo

    public Posts moderatePost(Integer postId, ModerationDTO dto) {
        // Validar que el moderador existe y tiene el rol correcto
        User moderator = userJpaRepository.findById(dto.getModeratorId()).orElse(null);
        if (moderator == null) {
            System.out.println("DEBUG: Moderador no encontrado: " + dto.getModeratorId());
            return null;
        }

        if (moderator.getUserType() != UserType.MODERATOR && moderator.getUserType() != UserType.ADMINISTRATOR) {
            System.out.println("DEBUG: Usuario sin permisos de moderación: " + dto.getModeratorId());
            return null;
        }

        // Buscar la publicación
        Posts post = postsJpaRepository.findById(postId).orElse(null);
        if (post == null) {
            System.out.println("DEBUG: Publicación no encontrada: " + postId);
            return null;
        }

        // Solo se pueden moderar publicaciones en PENDING
        if (post.getStatus() != PublicationStatus.PENDING) {
            System.out.println("DEBUG: La publicación no está en estado PENDING: " + postId);
            return null;
        }

        post.setStatus(dto.getStatus());
        return postsJpaRepository.save(post);
    }

    //Metodo que lista las publicaciones pendientes de moderación (solo para moderadores/administradores)
    public List<Posts> findPendingPosts(Integer moderatorId)
    {
        User moderator = userJpaRepository.findById(moderatorId).orElse(null);
        if (moderator == null) {
            System.out.println("DEBUG: Moderador no encontrado: " + moderatorId);
            return null;
        }

        if (moderator.getUserType() != UserType.MODERATOR && moderator.getUserType() != UserType.ADMINISTRATOR) {
            System.out.println("DEBUG: Usuario sin permisos de moderación: " + moderatorId);
            return null;
        }

        return postsJpaRepository.findByStatus(PublicationStatus.PENDING);
    }//Fin del metodo

    //Metodo que lista TODAS las publicaciones de un usuario, sin importar el estado (para su perfil propio)
    public List<Posts> findMyPosts(Integer userId)
    {
        return postsJpaRepository.findByUserId(userId);
    }//Fin del metodo
}
