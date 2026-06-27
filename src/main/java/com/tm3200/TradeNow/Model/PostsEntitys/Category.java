package com.tm3200.TradeNow.Model.PostsEntitys;

import jakarta.persistence.*;

@Entity
@Table( name = "tb_category")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String slug;//Versión del nombre en formato url

    @ManyToOne(fetch = FetchType.LAZY)//Solo carga la categoría si la pide
    @JoinColumn(name = "parent_id")
    private Category parent;//Sirve para subcategorías

    public Category() {
    }

    public Category(Integer id, String name, String slug, Category parent) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.parent = parent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
