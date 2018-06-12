package io.ermdev.papershelf.data.entity

import javax.persistence.*

@Entity
@Table(name = "tbl_book")
class Book(@Id
           @Column(name = "_id")
           var id: String = "",

           @Column(name = "_title")
           var title: String = "",

           @Column(name = "_status", length = 50)
           var status: String = "",

           @Column(name = "_summary", length = 500)
           var summary: String = "",

           @Column(name = "cover_image")
           var coverImage: String = "",

           @ManyToMany(cascade = [CascadeType.ALL])
           @JoinTable(name = "tbl_book_author", joinColumns = [JoinColumn(name = "book_id")],
                   inverseJoinColumns = [JoinColumn(name = "author_id")])
           var authors: MutableSet<Author> = HashSet(),

           @ManyToMany(cascade = [CascadeType.ALL])
           @JoinTable(name = "tbl_book_genre", joinColumns = [JoinColumn(name = "book_id")],
                   inverseJoinColumns = [JoinColumn(name = "genre_id")])
           var genres: MutableSet<Genre> = HashSet())