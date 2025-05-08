package com.sudoku.backend.controller;

import com.sudoku.backend.model.Score;
import com.sudoku.backend.model.User;
import com.sudoku.backend.repository.ScoreRepository;
import com.sudoku.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ScoreController {

    private final ScoreRepository scoreRepo;
    private final UserRepository userRepo;

    public ScoreController(ScoreRepository scoreRepo, UserRepository userRepo) {
        this.scoreRepo = scoreRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/scores/{email}")
    public List<Map<String, Object>> getScores(@PathVariable String email) {
        List<Score> scores = scoreRepo.findByEmail(email);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Score score : scores) {
            Map<String, Object> map = new HashMap<>();
            map.put("difficulty", score.getDifficulty());
            map.put("time_taken", score.getTime_taken());
            result.add(map);
        }

        return result;
    }

    @PostMapping("/scores")
    public Map<String, Object> saveScore(@RequestBody Score score) {
        scoreRepo.save(score);

        Optional<User> userOpt = userRepo.findById(score.getEmail());
        userOpt.ifPresent(user -> {
            int weight = switch (score.getDifficulty()) {
                case "easy" -> 10;
                case "medium" -> 100;
                case "hard" -> 1000;
                default -> 0;
            };

            int timeTaken = score.getTime_taken();
            if (timeTaken < 0) {
                timeTaken = 1;
            }
            int newlevel = user.getUserlevel() + weight/timeTaken ;

            user.setUserlevel(newlevel);
            userRepo.save(user);
        });

        return Map.of("success", true);
    }
    @GetMapping("/level/{email}")
    public Map<String, Object> getLevel(@PathVariable String email) {
        Optional<User> userOpt = userRepo.findById(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return Map.of("level", user.getUserlevel());
        } else {
            return Map.of("error", "User not found");
        }
    }
}
