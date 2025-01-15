package com.example.momogum.repository.photoRepo;

import com.example.momogum.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
}
