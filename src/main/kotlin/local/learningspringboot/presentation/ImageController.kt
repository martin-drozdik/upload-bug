package local.learningspringboot.presentation

import local.learningspringboot.service.ImageService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.IOException

const val BASE_PATH = "images"

@Controller
@RequestMapping(BASE_PATH)
class ImageController(val imageService: ImageService) {

    // curl -v -F file=@Downloads/haha.jpeg localhost:8888/images
    @PostMapping
    fun createImage(@RequestPart file: Flux<FilePart>): Mono<String>
    {
        return this.imageService.createImage(file)
            .then(Mono.just("redirect:/$BASE_PATH"))
    }
}