package com.rem.papershelf.rest.genre

import com.rem.papershelf.data.genre.Genre
import com.rem.papershelf.data.genre.GenreService
import com.rem.papershelf.data.genre.GenreSpecification
import com.rem.papershelf.exception.PaperShelfException
import com.rem.papershelf.rest.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/genres")
class GenreController(@Autowired val genreService: GenreService) {

    @GetMapping(produces = ["application/json", "application/hal+json"])
    fun getGenres(specification: GenreSpecification,
                  @PageableDefault(sort = ["name"], size = 20) pageable: Pageable): ResponseEntity<Any> {
        val resources = ArrayList<GenreDto>()
        genreService.findAll(specification, pageable).forEach({ genre ->
            val dto = GenreDto(id = genre.id, name = genre.name, description = genre.description)

            dto.add(linkTo(methodOn(this::class.java).getGenreById(genre.id)).withSelfRel())
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping(value = ["/{genreId}"], produces = ["application/json", "application/hal+json"])
    fun getGenreById(@PathVariable("genreId") genreId: String): ResponseEntity<Any> {
        return try {
            val genre = genreService.findById(genreId)
            val dto = GenreDto(id = genre.id, name = genre.name, description = genre.description)

            dto.add(linkTo(methodOn(this::class.java).getGenreById(genre.id)).withSelfRel())
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addGenre(@RequestBody body: GenreDto): ResponseEntity<Any> {
        return try {
            val genre = Genre(name = body.name, description = body.description)

            genreService.save(genre)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping(value = ["/{genreId}"], consumes = ["application/json"])
    fun updateGenreById(@PathVariable("genreId") genreId: String,
                        @RequestBody body: GenreDto): ResponseEntity<Any> {
        return try {
            val genre = genreService.findById(genreId)

            genre.name = body.name
            genre.description = body.description

            genreService.save(genre)
            ResponseEntity(HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{genreId}")
    fun deleteGenreById(@PathVariable("genreId") genreId: String): ResponseEntity<Any> {
        genreService.deleteById(genreId)
        return ResponseEntity(HttpStatus.OK)
    }
}