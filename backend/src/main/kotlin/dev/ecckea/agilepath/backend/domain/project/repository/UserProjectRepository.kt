package dev.ecckea.agilepath.backend.domain.project.repository

import dev.ecckea.agilepath.backend.domain.project.repository.entity.UserProjectEntity
import dev.ecckea.agilepath.backend.domain.project.repository.entity.UserProjectId
import org.springframework.data.jpa.repository.JpaRepository

interface UserProjectRepository : JpaRepository<UserProjectEntity, UserProjectId>, UserProjectRepositoryCustom
