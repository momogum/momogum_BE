package com.example.momogum.repository.reportRepo;

import com.example.momogum.domain.Report;
import com.example.momogum.domain.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
  boolean existsByReporterUserAndReportedUser(UserEntity reporter, UserEntity reportedUser);

  List<Report> findByReportedUser(UserEntity reportedUser);

  List<Report> findByReporterUser(UserEntity reporterUser);
}
