package local.learningspringboot.service

import org.springframework.core.io.ResourceLoader
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.Files
import java.nio.file.Paths

const val UPLOAD_ROOT = "upload-dir"

@Service
class ImageService(val resourceLoader: ResourceLoader)
{
    fun createImage(files: Flux<FilePart>): Mono<Void>
    {
        return files.flatMap {

            val path = Paths.get(UPLOAD_ROOT, it.filename())

            Files.deleteIfExists(path)
            Files.createFile(path)

            it.transferTo(path.toFile())

        }.then()
    }
}