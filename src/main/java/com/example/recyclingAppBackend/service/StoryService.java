package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.CreateStoryRequest;
import com.example.recyclingAppBackend.exception.ResourceNotFoundException;
import com.example.recyclingAppBackend.model.Story;
import com.example.recyclingAppBackend.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    public Story createStory(CreateStoryRequest request) {
        Story story = new Story();
        story.setTitle(request.getTitle());
        story.setDescription(request.getDescription());
        story.setNumberOfStages(request.getNumberOfStages());
        story.setLink(request.getLink());
        story.setImage(request.getImage());

        story.setMetal(request.getMetal());
        story.setGlass(request.getGlass());
        story.setPlastic(request.getPlastic());
        story.setOrganic(request.getOrganic());

        story.setActive(true);
        return storyRepository.save(story);
    }

    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    public Story getStoryById(String id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found with id: " + id));
    }

    public Story updateStory(String id, CreateStoryRequest request) {
        Story existingStory = getStoryById(id);
        existingStory.setTitle(request.getTitle());
        existingStory.setDescription(request.getDescription());
        existingStory.setNumberOfStages(request.getNumberOfStages());
        existingStory.setLink(request.getLink());
        existingStory.setId(request.getImage());
        return storyRepository.save(existingStory);
    }

    public void deleteStory(String id) {
        if (!storyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Story not found with id: " + id);
        }
        storyRepository.deleteById(id);
    }
}