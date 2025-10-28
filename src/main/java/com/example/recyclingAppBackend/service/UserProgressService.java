package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.UpdateProgressRequest;
import com.example.recyclingAppBackend.exception.ResourceNotFoundException;
import com.example.recyclingAppBackend.model.*;
import com.example.recyclingAppBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class UserProgressService {

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    public Story getStoryById(String id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found with id: " + id));
    }

    public UserProgress startStory(String userId, String storyId) {
        Optional<UserProgress> existingProgress = userProgressRepository.findByUserIdAndStoryId(userId, storyId);
        if (existingProgress.isPresent()) {
            UserProgress progress = existingProgress.get();
            // hydrate quiz scores before returning
            applyCourseScores(userId, progress);
            return progress;
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

        UserProgress saved = userProgressRepository.save(newProgress);

        // hydrate with day1/day2/day3 scores from StudentProfile
        applyCourseScores(userId, saved);

        return saved;
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

        UserProgress saved = userProgressRepository.save(progress);

        // hydrate quiz scores from StudentProfile before returning
        applyCourseScores(userId, saved);

        return saved;
    }

    public List<UserProgress> getProgressForUser(String userId) {
        List<UserProgress> list = userProgressRepository.findByUserId(userId);

        if (list.isEmpty()) {
            // ✅ Create a default initialized progress object
            UserProgress defaultProgress = new UserProgress();
            defaultProgress.setUserId(userId);
            defaultProgress.setStoryId(null); // No specific story yet
            defaultProgress.setCurrentStage(0);
            defaultProgress.setStatus(UserProgress.ProgressStatus.NOT_STARTED);
            defaultProgress.setLastPlayed(null);

            defaultProgress.setPlastic(0);
            defaultProgress.setGlass(0);
            defaultProgress.setOrganic(0);
            defaultProgress.setMetal(0);

            defaultProgress.setMarks(new ArrayList<>());
            defaultProgress.setDay1Score(0);
            defaultProgress.setDay2Score(0);
            defaultProgress.setDay3Score(0);

            // Hydrate with any quiz scores from StudentProfile
            applyCourseScores(userId, defaultProgress);

            // ✅ Optionally persist so it appears in DB next time
            userProgressRepository.save(defaultProgress);

            return List.of(defaultProgress);
        }

        // hydrate each item so parent /child/{childId} (and /me) sees quiz scores
        for (UserProgress p : list) {
            applyCourseScores(userId, p);
        }

        return list;
    }

    /**
     * Attach day1/day2/day3 quiz scores from StudentProfile to the given UserProgress.
     *
     * Why we do the two-step lookup:
     * - Sometimes the "userId" that gets passed in is the child's DB _id (like "6804...").
     * - Sometimes progress.getUserId() may be a different identifier (ex: username or older id format).
     *
     * We'll try both so we don't silently fall back to zeros.
     */
    private void applyCourseScores(String requestedUserId, UserProgress progress) {
        if (progress == null) {
            progress = new UserProgress();
            progress.setUserId(requestedUserId);
            progress.setDay1Score(0);
            progress.setDay2Score(0);
            progress.setDay3Score(0);
            progress.setPlastic(0);
            progress.setGlass(0);
            progress.setOrganic(0);
            progress.setMetal(0);
            progress.setCurrentStage(0);
            progress.setStatus(UserProgress.ProgressStatus.NOT_STARTED);
            progress.setMarks(new ArrayList<>());
            progress.setLastPlayed(null);
            return;
        }

        // 1. Try lookup by the ID the controller/service is using right now
        Optional<StudentProfile> profileOpt = studentProfileRepository.findByUserId(requestedUserId);

        // 2. Fallback: try whatever is stored on the progress row itself
        if (profileOpt.isEmpty() && progress.getUserId() != null) {
            profileOpt = studentProfileRepository.findByUserId(progress.getUserId());
        }

        if (profileOpt.isEmpty()) {
            // No profile at all -> default zeros
            progress.setDay1Score(0);
            progress.setDay2Score(0);
            progress.setDay3Score(0);
            return;
        }

        StudentProfile profile = profileOpt.get();
        QuizProgress qp = profile.getQuizProgress();
        if (qp == null) {
            progress.setDay1Score(0);
            progress.setDay2Score(0);
            progress.setDay3Score(0);
            return;
        }

        QuizDay day1 = qp.getDay1();
        QuizDay day2 = qp.getDay2();
        QuizDay day3 = qp.getDay3();

        progress.setDay1Score(extractScore(day1));
        progress.setDay2Score(extractScore(day2));
        progress.setDay3Score(extractScore(day3));
    }

    private Integer extractScore(QuizDay d) {
        if (d == null) return 0;
        if (d.getScore() == null) return 0;
        return d.getScore();
    }
}
