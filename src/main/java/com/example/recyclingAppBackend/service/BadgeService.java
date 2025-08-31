package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.CreateBadgeRequest;
import com.example.recyclingAppBackend.exception.ResourceNotFoundException;
import com.example.recyclingAppBackend.model.Badge;
import com.example.recyclingAppBackend.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    public Badge createBadge(CreateBadgeRequest request) {
        Badge badge = new Badge();
        badge.setName(request.getName());
        badge.setDescription(request.getDescription());
        badge.setIconUrl(request.getIconUrl());
        return badgeRepository.save(badge);
    }

    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    public Badge getBadgeById(String id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Badge not found with id: " + id));
    }

    public Badge updateBadge(String id, CreateBadgeRequest request) {
        Badge existingBadge = getBadgeById(id); // Throws exception if not found
        existingBadge.setName(request.getName());
        existingBadge.setDescription(request.getDescription());
        existingBadge.setIconUrl(request.getIconUrl());
        return badgeRepository.save(existingBadge);
    }

    public void deleteBadge(String id) {
        if (!badgeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Badge not found with id: " + id);
        }
        badgeRepository.deleteById(id);
    }
}