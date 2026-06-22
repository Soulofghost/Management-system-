package com.library.service;

import com.library.dto.PublisherDTO;
import com.library.entity.Publisher;
import com.library.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final ModelMapper modelMapper;

    public PublisherDTO createPublisher(PublisherDTO publisherDTO, Integer userId) {
        Publisher publisher = modelMapper.map(publisherDTO, Publisher.class);
        publisher.setCreatedBy(userId);
        publisher.setIsActive(true);
        publisher.setCreatedAt(LocalDateTime.now());
        publisher.setUpdatedAt(LocalDateTime.now());

        Publisher savedPublisher = publisherRepository.save(publisher);
        return modelMapper.map(savedPublisher, PublisherDTO.class);
    }

    public PublisherDTO updatePublisher(Integer publisherId, PublisherDTO publisherDTO, Integer userId) {
        Publisher publisher = publisherRepository.findById(publisherId)
            .orElseThrow(() -> new RuntimeException("Publisher not found"));

        publisher.setPublisherName(publisherDTO.getPublisherName());
        publisher.setEmail(publisherDTO.getEmail());
        publisher.setPhone(publisherDTO.getPhone());
        publisher.setAddress(publisherDTO.getAddress());
        publisher.setCity(publisherDTO.getCity());
        publisher.setState(publisherDTO.getState());
        publisher.setCountry(publisherDTO.getCountry());
        publisher.setPostalCode(publisherDTO.getPostalCode());
        publisher.setWebsiteUrl(publisherDTO.getWebsiteUrl());
        publisher.setUpdatedAt(LocalDateTime.now());

        Publisher updatedPublisher = publisherRepository.save(publisher);
        return modelMapper.map(updatedPublisher, PublisherDTO.class);
    }

    public void deletePublisher(Integer publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
            .orElseThrow(() -> new RuntimeException("Publisher not found"));
        publisherRepository.delete(publisher);
    }

    public PublisherDTO getPublisherById(Integer publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
            .orElseThrow(() -> new RuntimeException("Publisher not found"));
        return modelMapper.map(publisher, PublisherDTO.class);
    }

    public List<PublisherDTO> getAllPublishers() {
        return publisherRepository.findAllByOrderByPublisherNameAsc()
            .stream()
            .map(publisher -> modelMapper.map(publisher, PublisherDTO.class))
            .collect(Collectors.toList());
    }

    public List<PublisherDTO> getActivePublishers() {
        return publisherRepository.findAllByIsActiveTrueOrderByPublisherNameAsc()
            .stream()
            .map(publisher -> modelMapper.map(publisher, PublisherDTO.class))
            .collect(Collectors.toList());
    }
}
