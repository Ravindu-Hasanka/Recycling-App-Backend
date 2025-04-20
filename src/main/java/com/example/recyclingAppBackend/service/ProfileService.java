package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.model.LearningPath;
import com.example.recyclingAppBackend.model.StudentProfile;
import com.example.recyclingAppBackend.model.User;
import com.example.recyclingAppBackend.repository.StudentProfileRepository;
import com.example.recyclingAppBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProfileService {
    @Autowired
    private StudentProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public StudentProfile getOrInitProfile(String userId) {
        return profileRepository.findById(userId).orElseGet(() -> {
            StudentProfile sp = new StudentProfile();
            sp.setUserId(userId);
            sp.setViewHistory(new ArrayList<>());
            sp.setCompletedPaths(new ArrayList<>());
            sp.setOngoingPaths(new ArrayList<>());
            sp.setLikedPaths(new ArrayList<>());
            sp.setTotalPoints(0);
            return profileRepository.save(sp);
        });
    }

    public StudentProfile updateHistory(String userId, String itemId) {
        User user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        StudentProfile sp = getOrInitProfile(user.getId());
        sp.getViewHistory().add(itemId);
        return profileRepository.save(sp);
    }

    public StudentProfile completePath(String userId, LearningPath path) {
        User user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        StudentProfile sp = getOrInitProfile(user.getId());
        sp.getOngoingPaths().remove(path.getId());
        sp.getCompletedPaths().add(path.getId());
        sp.setTotalPoints(sp.getTotalPoints() + path.getPoints());
        return profileRepository.save(sp);
    }
}
