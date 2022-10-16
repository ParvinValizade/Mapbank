package com.company.map.model

import java.time.Instant
import javax.persistence.*

@Entity
data class RefreshToken @JvmOverloads constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,
    @Column(nullable = false, unique = true)
    val token: String,
    val expiryDate: Instant
)
