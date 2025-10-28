package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.LevelInfoResponse;
import com.example.recyclingAppBackend.dto.FullLevelStatusResponse;
import com.example.recyclingAppBackend.model.*;
import com.example.recyclingAppBackend.repository.StudentProfileRepository;
import com.example.recyclingAppBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private StudentProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Ensure the student profile exists for this userId.
     * userId here is the internal User.id (the value you're getting from principal.getName()).
     */
    public StudentProfile getOrInitProfile(String userId) {
        return profileRepository.findByUserId(userId).orElseGet(() -> {
            StudentProfile sp = StudentProfile.builder()
                    .userId(userId)
                    .age(null)
                    .course(null)
                    .quizProgress(new QuizProgress())
                    .points(0)
                    .build();
            return profileRepository.save(sp);
        });
    }

    /**
     * PUT /api/student-course/day/{day}/score
     */
    public void updateDayScore(String userId, int day, int score) {
        if (day < 1 || day > 3) {
            throw new IllegalArgumentException("day must be 1, 2, or 3");
        }

        StudentProfile sp = getOrInitProfile(userId);

        QuizProgress qp = sp.getQuizProgress();
        if (qp == null) {
            qp = new QuizProgress();
            sp.setQuizProgress(qp);
        }

        QuizDay target = getQuizDayRef(qp, day);
        target.setScore(score);

        profileRepository.save(sp);
    }

    /**
     * PUT /api/student-course/day/{day}/status
     */
    public void updateDayStatus(String userId, int day, QuizStatus status) {
        if (day < 1 || day > 3) {
            throw new IllegalArgumentException("day must be 1, 2, or 3");
        }

        StudentProfile sp = getOrInitProfile(userId);

        QuizProgress qp = sp.getQuizProgress();
        if (qp == null) {
            qp = new QuizProgress();
            sp.setQuizProgress(qp);
        }

        QuizDay target = getQuizDayRef(qp, day);
        target.setStatus(status);

        profileRepository.save(sp);
    }

    /**
     * GET /api/student-course/level
     *
     * NOW:
     * - We do NOT use score anymore.
     * - We do NOT do difficulty logic with >50 etc.
     * - We only use age to decide levelCode.
     *
     * Age logic:
     *   if 7 < age < 10 => levelCode = 3
     *   else if 3 < age < 7 => levelCode = 2
     *   else => levelCode = 1
     *
     * ageGroup stays:
     *   1 <= age < 3  => 1
     *   3 <= age < 7  => 2
     *   7 <= age < 10 => 3
     *   else          => 0
     *
     * levelText will just be mapped from that code:
     *   1 -> "EASY"
     *   2 -> "MEDIUM"
     *   3 -> "HARD"
     */
    public LevelInfoResponse getLevelInfo(String userId) {
        StudentProfile sp = getOrInitProfile(userId);

        Integer age = safeAge(sp);
        int levelCodeFromAge = mapAgeToOverallLevel(age);
        String levelTextFromAge = mapLevelCodeToText(levelCodeFromAge);

        int ageGroup = mapAgeToGroup(age);

        return new LevelInfoResponse(
                levelTextFromAge, // EASY / MEDIUM / HARD
                levelCodeFromAge, // 1 / 2 / 3
                age,
                ageGroup
        );
    }

    /**
     * GET /api/student-course/level/full
     *
     * We return:
     * - "overall" (based on age bucket only)
     * - per-day info
     *
     * For each day, difficultyCode/difficultyText are now STATIC by day:
     *   Day 1 -> EASY / 1
     *   Day 2 -> MEDIUM / 2
     *   Day 3 -> HARD / 3
     *
     * Score and status are still whatever the student has.
     */
    public FullLevelStatusResponse getFullLevelStatus(String userId) {
        StudentProfile sp = getOrInitProfile(userId);

        Integer age = safeAge(sp);


        Optional<User> userAge = userRepository.findById(userId);
        User user = userAge.orElseThrow(() -> new IllegalStateException("User not found"));
        age = user.getAge();
        int ageGroup = mapAgeToGroup(age);
        // Build quizProgress if null
        QuizProgress qp = sp.getQuizProgress();
        if (qp == null) {
            qp = new QuizProgress();
            sp.setQuizProgress(qp);
        }

        QuizDay d1 = qp.getDay1();
        QuizDay d2 = qp.getDay2();
        QuizDay d3 = qp.getDay3();

        int d1Score = safeScore(d1);
        int d2Score = safeScore(d2);
        int d3Score = safeScore(d3);

        // Day-based difficulty (no score logic)
        int d1Code = 1;
        int d2Code = 2;
        int d3Code = 3;

        String d1Text = mapLevelCodeToText(d1Code); // EASY
        String d2Text = mapLevelCodeToText(d2Code); // MEDIUM
        String d3Text = mapLevelCodeToText(d3Code); // HARD

        int overallCode;
        // overall difficulty is purely from age bucket now
        if (d1.getStatus() == null || d1.getStatus() == QuizStatus.NOT_START){
            overallCode = 1;
        }
        else if (d2.getStatus() == null || d2.getStatus() == QuizStatus.NOT_START){
            overallCode = 2;
        }
        else{
            overallCode = 3;
        }

        String overallText = mapLevelCodeToText(overallCode);

        return FullLevelStatusResponse.builder()
                .overallLevelText(overallText)
                .overallLevelCode(overallCode)
                .age(age)
                .ageGroup(ageGroup)

                .day1Score(d1Score)
                .day1Status(d1 != null && d1.getStatus() != null ? d1.getStatus().name() : "NOT_START")
                .day1DifficultyText(d1Text)
                .day1DifficultyCode(d1Code)

                .day2Score(d2Score)
                .day2Status(d2 != null && d2.getStatus() != null ? d2.getStatus().name() : "NOT_START")
                .day2DifficultyText(d2Text)
                .day2DifficultyCode(d2Code)

                .day3Score(d3Score)
                .day3Status(d3 != null && d3.getStatus() != null ? d3.getStatus().name() : "NOT_START")
                .day3DifficultyText(d3Text)
                .day3DifficultyCode(d3Code)

                .build();
    }

    // ---- helpers ----

    private QuizDay getQuizDayRef(QuizProgress qp, int day) {
        return switch (day) {
            case 1 -> qp.getDay1();
            case 2 -> qp.getDay2();
            case 3 -> qp.getDay3();
            default -> throw new IllegalArgumentException("day must be 1, 2, or 3");
        };
    }

    private Integer safeAge(StudentProfile sp) {
        Integer age = sp.getAge();
        return (age != null) ? age : 0;
    }

    private int safeScore(QuizDay quizDay) {
        if (quizDay == null) return 0;
        Integer s = quizDay.getScore();
        return (s != null) ? s : 0;
    }

    /**
     * Map age to "overall level code".
     * if 7 < age < 10 => 3
     * else if 3 < age < 7 => 2
     * else => 1
     */
    private int mapAgeToOverallLevel(int age) {
        if (age > 7 && age < 10) {
            return 3;
        }
        if (age > 3 && age < 7) {
            return 2;
        }
        return 1;
    }

    /**
     * Convert numeric level code to label.
     * 1 -> EASY
     * 2 -> MEDIUM
     * 3 -> HARD
     */
    private String mapLevelCodeToText(int code) {
        return switch (code) {
            case 1 -> "EASY";
            case 2 -> "MEDIUM";
            case 3 -> "HARD";
            default -> "EASY";
        };
    }

    /**
     * Age group codes:
     * 1 <= age < 3  => 1
     * 3 <= age < 7  => 2
     * 7 <= age < 10 => 3
     * else          => 0
     *
     * (this is the same logic you already approved earlier)
     */
    private int mapAgeToGroup(int age) {
        if (age >= 1 && age < 3) return 1;
        if (age >= 3 && age < 7) return 2;
        if (age >= 7 && age < 10) return 3;
        return 0;
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
