package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.UpdateProgressRequest;
import com.example.recyclingAppBackend.exception.ResourceNotFoundException;
import com.example.recyclingAppBackend.model.Story;
import com.example.recyclingAppBackend.model.UserProgress;
import com.example.recyclingAppBackend.repository.StoryRepository;
import com.example.recyclingAppBackend.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserProgressService {

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private StoryRepository storyRepository;

    public Story getStoryById(String id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found with id: " + id));
    }

    public UserProgress startStory(String userId, String storyId) {
        Optional<UserProgress> existingProgress = userProgressRepository.findByUserIdAndStoryId(userId, storyId);
        if (existingProgress.isPresent()) {
            return existingProgress.get();
        }

        storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot start progress. Story not found with id: " + storyId));

        Story story = getStoryById(storyId);
        UserProgress newProgress = new UserProgress();
        newProgress.setUserId(userId);
        newProgress.setStoryId(storyId);
        newProgress.setCurrentStage(1);
        newProgress.setStatus(UserProgress.ProgressStatus.IN_PROGRESS);
        newProgress.setLastPlayed(LocalDateTime.now());

        newProgress.setMetal(story.getMetal());
        newProgress.setGlass(story.getGlass());
        newProgress.setPlastic(story.getPlastic());
        newProgress.setOrganic(story.getOrganic());

        return userProgressRepository.save(newProgress);
    }

    public UserProgress updateProgress(String userId, UpdateProgressRequest request) {
        UserProgress progress = userProgressRepository.findByUserIdAndStoryId(userId, request.getStoryId())
                .orElseThrow(() -> new ResourceNotFoundException("No progress found for this user and story."));

        Story story = storyRepository.findById(request.getStoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Story not found with id: " + request.getStoryId()));

        progress.getMarks().add(request.getScore());
        progress.setLastPlayed(LocalDateTime.now());

        if (progress.getCurrentStage() < story.getNumberOfStages()) {
            progress.setCurrentStage(progress.getCurrentStage() + 1);
        } else {
            progress.setStatus(UserProgress.ProgressStatus.COMPLETED);
        }

        return userProgressRepository.save(progress);
    }

    public List<UserProgress> getProgressForUser(String userId) {
        return userProgressRepository.findByUserId(userId);
    }
}