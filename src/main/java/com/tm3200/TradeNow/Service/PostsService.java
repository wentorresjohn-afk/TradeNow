package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.DTO.PostsDTO;
import com.tm3200.TradeNow.Model.Enum.PublicationStatus;
import com.tm3200.TradeNow.Model.Enum.PublicationType;
import com.tm3200.TradeNow.Model.Posts;
import com.tm3200.TradeNow.Repository.PostsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostsService
{
    @Autowired
    private PostsJpaRepository postsJpaRepository;

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
    public Posts addPost (PostsDTO dto)
    {
        //Verifica si el usuario crea una oferta pero no manda el valor estimado
        if (dto.getType() == PublicationType.OFFER && dto.getEstimatedValue() == null)
        {
            return null;
        }//Fin del if

        //Verifica si el usuario crea una busqueda pero no indica que ofrece a cambio
        if (dto.getType() == PublicationType.SEARCH && (dto.getExchangeFor() == null || dto.getExchangeFor().isBlank()) )
        {
            return null;
        }//Fin del if

        Posts posts = new Posts();
        posts.setType(dto.getType());
        posts.setTitle(dto.getTitle());
        posts.setDescription(dto.getDescription());
        posts.setEstimatedValue(dto.getEstimatedValue());
        posts.setExchangeFor(dto.getExchangeFor());
        posts.setStatus(PublicationStatus.PENDING);

        return postsJpaRepository.save(posts);
    }//Fin del metodo

    //Editar una publicación propia
    public Posts updatePost(Integer id, PostsDTO dto)
    {
        Posts posts = postsJpaRepository.findById(id).orElse(null);

        if (posts == null)
        {
            return null;
        }

        if (dto.getType() == PublicationType.OFFER && dto.getEstimatedValue()== null)
        {
            return null;
        }//Fin del if

        if (dto.getType() == PublicationType.SEARCH && (dto.getExchangeFor()==null || dto.getExchangeFor().isBlank()))
        {
            return null;
        }//Fin del if

        posts.setType(dto.getType());
        posts.setTitle(dto.getTitle());
        posts.setDescription(dto.getDescription());
        posts.setEstimatedValue(dto.getEstimatedValue());
        posts.setExchangeFor(dto.getExchangeFor());

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
