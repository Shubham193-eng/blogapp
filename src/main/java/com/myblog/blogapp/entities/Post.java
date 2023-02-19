package com.myblog.blogapp.entities;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
//when we applied lombok annotation we need not add setter and getter, it remove boiler plate code .

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name="post",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}

        )
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="title",nullable=false)
    private String title;
    @Column(name="description",nullable = false)
    private String description;
    //@lob if u want more than 255 char than u have to use this
    @Column(name="content",nullable = false)
    private String content;//255 char

    @OneToMany(mappedBy = "post",cascade =CascadeType.ALL,orphanRemoval = true )
    //cascade indicates that whatever the changes made by post effect on comment,orphanRemovel remove the cmnt when post is deleted.
    Set<Comment> comments=new HashSet<>();






}

