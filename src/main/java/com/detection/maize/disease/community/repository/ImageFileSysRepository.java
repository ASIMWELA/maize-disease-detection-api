package com.detection.maize.disease.community.repository;

import com.detection.maize.disease.community.entity.FileSystemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileSysRepository extends JpaRepository<FileSystemImage, Long> {
}
