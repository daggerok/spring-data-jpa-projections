package com.github.daggerok.springdatajpaprojections

import org.apache.logging.log4j.kotlin.logger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.transaction.support.TransactionTemplate

@SqlGroup(
    Sql(scripts = ["classpath:/sql-scripts/drop-ddl.sql"], executionPhase = AFTER_TEST_METHOD),
    // Sql(scripts = ["classpath:/sql-scripts/ddl.sql"], executionPhase = BEFORE_TEST_METHOD),
    Sql("classpath:/sql-scripts/ddl.sql"),
)
@TestInstance(PER_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class SpringDataJpaProjectionsApplicationTests @Autowired constructor(
    val transactionTemplate: TransactionTemplate,
    val addressRepository: AddressRepository,
    val personRepository: PersonRepository,
) {

    @Test
    fun `should test projections`() {
        // setup
        transactionTemplate.execute {
            personRepository.deleteAllInBatch()
        }

        // given
        transactionTemplate.execute {
            personRepository.save(
                Person(
                    firstName = "Max",
                    lastName = "Maksimko",
                    address = Address(
                        country = "Ukraine",
                        city = "Odessa",
                        street = "hz",
                    ),
                )
            )
        }

        // and
        transactionTemplate.execute {
            personRepository.save(
                Person(
                    firstName = "Bax",
                    lastName = "Fax",
                    address = Address(
                        country = "Ukraine",
                        city = "Kiev",
                        street = "hz",
                    ),
                )
            )
        }

        // when
        val views = personRepository.findOloloTrololoBy()
        logger().info { "views: $views" }
        logger().info { "first names: ${views.map { it.firstName }}" }

        // then
        assertThat(views).hasSize(2)
        assertThat(views.first().firstName).isEqualTo("Max")
    }

    @Test
    fun `should test cascade`() {
        // setup
        transactionTemplate.execute {
            personRepository.deleteAllInBatch()
        }

        // given
        val address = transactionTemplate.execute {
            addressRepository.save(
                Address(
                    country = "Ukraine",
                    city = "Odessa",
                    street = "hz",
                )
            )
        } ?: fail("address may not be null")

        // and
        transactionTemplate.execute {
            personRepository.save(
                Person(
                    firstName = "Max",
                    lastName = "Maksimko",
                    address = address,
                )
            )
        }

        // and
        transactionTemplate.execute {
            personRepository.save(
                Person(
                    firstName = "Bax",
                    lastName = "Fax",
                    address = address,
                )
            )
        }

        // // and
        val addresses = addressRepository.findAll()
        assertThat(addresses).hasSize(1)

        // when
        transactionTemplate.execute {
            val first = personRepository.findAll().first()
            personRepository.delete(first)
        }

        // then
        val people = personRepository.findAll()
        assertThat(people)
            .isNotEmpty
            .hasSize(1)
    }

    @Test
    fun `should test cascade creations`() {
        // setup
        transactionTemplate.execute {
            personRepository.deleteAllInBatch()
        }

        // given
        transactionTemplate.execute {
            personRepository.save(
                Person(
                    firstName = "Max",
                    lastName = "Maksimko",
                    address = Address(
                        country = "Ukraine",
                        city = "Odessa",
                        street = "hz",
                    ),
                )
            )
        }

        // and
        transactionTemplate.execute {
            personRepository.save(
                Person(
                    firstName = "Bax",
                    lastName = "Fax",
                    address = Address(
                        country = "Ukraine",
                        city = "Odessa",
                        street = "hz",
                    ),
                )
            )
        }

        // // and
        // val addresses = addressRepository.findAll()
        // assertThat(addresses).hasSize(2)

        // when
        transactionTemplate.execute {
            val first = personRepository.findAll().first()
            personRepository.delete(first)
        }

        // then
        val people = personRepository.findAll()
        assertThat(people)
            .isNotEmpty
            .hasSize(1)
    }

    @Test
    fun `should save person with address`() {
        // setup
        transactionTemplate.execute {
            personRepository.deleteAllInBatch()
            // addressRepository.deleteAllInBatch()
        }

        // given
        assertThat(personRepository.findAll()).isEmpty()

        // // when
        // val address = transactionTemplate.execute {
        //     addressRepository.save(
        //         Address(
        //             country = "Ukraine",
        //             city = "Odessa",
        //             street = "hz",
        //         )
        //     )
        // } ?: fail("address may not be null")

        // and
        transactionTemplate.execute {
            personRepository.save(
                Person(
                    firstName = "Maksim",
                    lastName = "Kostromin",
                    // address = address,
                    address = Address(
                        country = "Ukraine",
                        city = "Odessa",
                        street = "hz",
                    ),
                )
            )
        }

        // then
        val people = personRepository.findAll()
        assertThat(people)
            .isNotEmpty
            .hasSize(1)
    }
}
