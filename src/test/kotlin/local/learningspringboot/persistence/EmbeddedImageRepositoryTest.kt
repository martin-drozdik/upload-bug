package local.learningspringboot.persistence

import local.learningspringboot.model.Image
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@DataMongoTest
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmbeddedImageRepositoryTest
{
  @Autowired
  lateinit var repository: ImageRepository

  @Autowired
  lateinit var operations: MongoOperations


  @BeforeAll
  fun setup()
  {
    operations.dropCollection(Image::class.java)

    operations.insert(Image(name = "learning_1.jpg"))
    operations.insert(Image(name = "learning_2.jpg"))
    operations.insert(Image(name = "learning_3.jpg"))

    operations.findAll(Image::class.java).forEach(::println)
  }


  @Test
  fun `find all books should work`()
  {
    val images: Flux<Image> = repository.findAll()

    StepVerifier.create(images)
      .recordWith(::ArrayList)
      .expectNextCount(3)
      .consumeRecordedWith { results ->
        assertThat(results).hasSize(3)
        assertThat(results)
          .extracting<String>(Image::name)
          .contains("learning_1.jpg", "learning_2.jpg", "learning_3.jpg")
      }
      .expectComplete()
      .verify()
  }


  @Test
  fun `find one book should work`()
  {
    val image: Mono<Image> = repository.findByName("learning_1.jpg")

    StepVerifier.create(image)
      .consumeNextWith { image ->
        assertThat(image.name).isEqualTo("learning_1.jpg")
      }
      .expectComplete()
      .verify()
  }
}