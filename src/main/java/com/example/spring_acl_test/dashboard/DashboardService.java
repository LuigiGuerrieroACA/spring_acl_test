package com.example.spring_acl_test.dashboard;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {

    private final DashboardRepository repository;

    public DashboardService(DashboardRepository repository) {
        this.repository = repository;
    }

    public List<Dashboard> findAll() {
        return repository.findAll();
    }

    public Optional<Dashboard> findById(String id) {
        return repository.findById(id);
    }
}
