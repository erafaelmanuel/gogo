package com.rem.papershelf.data.genre

import com.rem.papershelf.exception.EntityException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

@Service
class GenreService(@Autowired val genreRepository: GenreRepository) {

    fun findAll(specification: GenreSpecification, pageable: Pageable): Page<Genre> {
        return genreRepository.findAll(specification, pageable)
    }

    fun findById(id: String): Genre {
        return genreRepository.findById(id).orElseThrow({
            EntityException("No genre with id '$id' exists!")
        })
    }

    fun save(genre: Genre) {
        if (StringUtils.isEmpty(genre.name)) {
            throw EntityException("name cannot be empty")
        }
        if (!genre.name.matches(Regex("^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$"))) {
            throw EntityException("name cannot contain special characters")
        }
        if (StringUtils.isEmpty(genre.id)) {
            genre.id = UUID.randomUUID().toString()
        }
        genreRepository.save(genre)
    }

    fun deleteById(genreId: String) = genreRepository.deleteById(genreId)
}