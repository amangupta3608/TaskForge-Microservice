package com.Task_Forge.Microservice.Repository;

import com.Task_Forge.Microservice.Entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
}
