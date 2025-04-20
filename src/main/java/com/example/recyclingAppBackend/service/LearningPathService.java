package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.CreateLearningPathRequest;
import com.example.recyclingAppBackend.model.LearningPath;
import com.example.recyclingAppBackend.model.Section;
import com.example.recyclingAppBackend.model.SubSection;
import com.example.recyclingAppBackend.model.User;
import com.example.recyclingAppBackend.repository.LearningPathRepository;
import com.example.recyclingAppBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LearningPathService {
    @Autowired
    private LearningPathRepository pathRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    public LearningPath createPath(CreateLearningPathRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        System.out.println(user);
        LearningPath p = new LearningPath();
        p.setTitle(request.title());
        p.setDescription(request.description());
        p.setUploaderId(user.getId());
        p.setLabels(request.labels());
        p.setPoints(request.points());

        for (Section sect : request.sections()) {
            sect.setId(UUID.randomUUID().toString());
            for (SubSection sub : sect.getSubSections()) {
                sub.setId(UUID.randomUUID().toString());
            }
        }
        p.setSections(request.sections());

        return pathRepository.save(p);
    }

    public List<LearningPath> listPaths() {
        return pathRepository.findAll();
    }

    public LearningPath getPath(String id) {
        return pathRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Learning path not found: " + id));
    }

    public LearningPath updatePath(String id, com.example.recyclingAppBackend.dto.CreateLearningPathRequest request) {
        LearningPath p = getPath(id);
        p.setTitle(request.title());
        p.setDescription(request.description());
        p.setLabels(request.labels());
        p.setSections(request.sections());
        p.setPoints(request.points());
        return pathRepository.save(p);
    }

    public void deletePath(String id) {
        pathRepository.deleteById(id);
    }

    public void completePath(String userId, String pathId) {
        LearningPath p = getPath(pathId);
        profileService.completePath(userId, p);
    }
}
