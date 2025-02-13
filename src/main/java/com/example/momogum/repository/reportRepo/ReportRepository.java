package com.example.momogum.repository.reportRepo;

import com.example.momogum.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
