package com.github.daggerok.springdatajpaprojections

import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator
import javax.validation.constraints.NotBlank
import org.hibernate.annotations.CreationTimestamp
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.format.annotation.DateTimeFormat

@Entity
data class Address(

    @NotBlank
    @Column(nullable = false)
    val country: String = "",

    @NotBlank
    @Column(nullable = false)
    val city: String = "",

    @NotBlank
    @Column(nullable = false)
    val street: String = "",

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "Address")
    @SequenceGenerator(name = "Address", sequenceName = "address_sequence", initialValue = 111, allocationSize = 1)
    val id: Long = -1,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime? = null,

    @CreationTimestamp
    @Column(nullable = false, updatable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime? = null,
)/* {
    override fun hashCode(): Int = Objects.hash(country, city, street)

    override fun equals(other: Any?): Boolean =
        (other is Address)
          && Objects.equals(other.country, country)
          && Objects.equals(other.city, city)
          && Objects.equals(other.street, street)
}*/

interface AddressRepository : JpaRepository<Address, Long>

@Entity
data class Person(

    @NotBlank
    @Column(nullable = false)
    val firstName: String = "",

    @NotBlank
    @Column(nullable = false)
    val lastName: String = "",

    // Using cascade just because I don't want to save address first before person creation:
    @OneToOne(/*fetch = EAGER, */cascade = [CascadeType.ALL])
    @JoinColumn(foreignKey = ForeignKey(name = "person_address_id_fk"))
    val address: Address? = null,

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "Person")
    @SequenceGenerator(name = "Person", sequenceName = "person_sequence", initialValue = 222, allocationSize = 1)
    val id: Long = -1,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime? = null,

    @CreationTimestamp
    @Column(nullable = false, updatable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime? = null,
)/* {
    override fun hashCode(): Int = Objects.hash(firstName, lastName, address)

    override fun equals(other: Any?): Boolean =
        (other is Person)
          && Objects.equals(other.firstName, firstName)
          && Objects.equals(other.lastName, lastName)
          && Objects.equals(other.address, address)
}*/

interface PersonFirstNameInterfaceView {
    val firstName: String
}

data class PersonFirstNameClassView(val firstName: String)

interface PersonRepository : JpaRepository<Person, Long> {
    fun getAllFirstNamesBy(): List<PersonFirstNameInterfaceView>
    fun findOloloTrololoBy(): List<PersonFirstNameClassView>
    fun findAllBy(): List<PersonFirstNameInterfaceView>
    fun findBy(): List<PersonFirstNameInterfaceView>
    fun getBy(): List<PersonFirstNameClassView>
}

@SpringBootApplication
class SpringDataJpaProjectionsApplication

fun main(args: Array<String>) {
    runApplication<SpringDataJpaProjectionsApplication>(*args)
}
