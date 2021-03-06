package com.example.crowdfunding.reward;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

public class RewardConfig {
    @Bean
    CommandLineRunner rewardRepoCommandLineRunner(RewardRepository repository) {
        return args -> { repository.findAll(); };
    }

    @Bean
    public Reward reward(){
        return new Reward();
    }
}
