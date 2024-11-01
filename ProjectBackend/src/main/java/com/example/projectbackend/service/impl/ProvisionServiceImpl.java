package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.ProvisionRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.bean.response.ProvisionResponse;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.ImageMapper;
import com.example.projectbackend.mapper.ProvisionMapper;
import com.example.projectbackend.repository.ImageRepository;
import com.example.projectbackend.repository.ProvisionRepository;
import com.example.projectbackend.service.ProvisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProvisionServiceImpl implements ProvisionService {
    private final ProvisionRepository provisionRepository;
    private final ImageRepository imageRepository;


    @Override
    public List<ProvisionResponse> getAllProvision() {
        List<Provision> provisions = provisionRepository.findAll();

        if (provisions.isEmpty()) {
            throw new EmptyListException("ProvisionEmpty", "This Provision list is empty");
        }

        return provisions.stream()
                .map(provision -> {
                    ProvisionResponse provisionResponse = ProvisionMapper.convertToResponse(provision);

                    // Lấy danh sách ảnh và chuyển đổi chúng sang ImageResponse
                    List<ImageResponse> images = imageRepository.findAllByNameAndReferenceId("PROVISION", provision.getProvisionId())
                            .stream()
                            .map(ImageMapper::convertToResponse)
                            .collect(Collectors.toList());

                    provisionResponse.setImages(images);
                    return provisionResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProvisionResponse getDetailProvision(Long provisionId) {
        Provision provision = provisionRepository.findById(provisionId).orElse(null);
        if(Objects.isNull(provision)){
            throw new NotFoundException("ProvisionNotFound","Not found Provision with " + provisionId);
        }
        return ProvisionMapper.convertToResponse(provisionRepository.findById(provisionId).orElse(null));
    }

    @Override
    public Provision createProvision(ProvisionRequest provisionRequest) {
        Provision provision = ProvisionMapper.convertFromRequest(provisionRequest);
        return provisionRepository.save(provision);
    }

    @Override
    public Provision updateProvision(Long provisionId, Provision provision) {
        Provision provisionUpdate = provisionRepository.findById(provisionId).orElse(null);
        if(provisionUpdate != null){
            setProvision(provisionUpdate, provision);
        }
        return provisionRepository.save(provisionUpdate);
    }


    @Override
    public void deleteProvision(Long provisionId) {
        Optional<Provision> provision = provisionRepository.findById(provisionId);
        if (provision.isEmpty()) {
            throw new NotFoundException("ProvisionNotFound","Provision with ID " + provisionId + " not found.");
        }
        provisionRepository.deleteById(provisionId);
    }

    public void setProvision(Provision provisionUpdate, Provision provisionInput) {
        provisionUpdate.setProvisionName(provisionInput.getProvisionName());
        provisionUpdate.setDescription(provisionInput.getDescription());
        provisionUpdate.setStatus(provisionInput.getStatus());
        provisionUpdate.setPrice(provisionInput.getPrice());
    }
}
