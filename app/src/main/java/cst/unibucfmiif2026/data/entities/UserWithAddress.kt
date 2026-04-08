package cst.unibucfmiif2026.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithAddress(
    @Embedded
    val user : UserEntity,
    @Relation(
        parentColumn = UserEntity.ARG_ID,
        entityColumn = AddressEntity.ARG_ID
    )
    // TODO Review this relation
    val address : AddressEntity
)
