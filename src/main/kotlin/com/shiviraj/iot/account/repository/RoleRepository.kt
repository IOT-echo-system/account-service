package com.shiviraj.iot.account.repository

import com.shiviraj.iot.account.model.Role
import com.shiviraj.iot.account.model.RoleId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface RoleRepository : ReactiveCrudRepository<Role, RoleId> {
    fun findByRoleId(roleId: RoleId): Mono<Role>
}
