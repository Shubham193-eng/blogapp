package com.myblog.blogapp.entities;
//it is the code of ER-digram (entity relationship mapping ),one to many mapping .
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="comments")
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String body;
    private String email;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)//the comment class is map to this post entity many to one.
    @JoinColumn(name="post_id",nullable = false)
    //nullable =false it indicates that it should be not null there should be a column with this name.
    //by using this annotation we can join two tables with the specific foreign key.
    private Post post;
}
