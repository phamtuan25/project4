package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.ProvisionRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.bean.response.ProvisionResponse;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.ImageMapper;
import com.example.projectbackend.mapper.ProvisionMapper;
import com.example.projectbackend.repository.ImageRepository;
import com.example.projectbackend.repository.ProvisionRepository;
import com.example.projectbackend.service.ProvisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
    public Page<ProvisionResponse> getAllProvision(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                   @RequestParam(required = false) String keyword) {
        Specification<Provision> spec = searchByKeyword(keyword);
        Page<Provision> provisionPage = provisionRepository.findAll(spec, pageable);

        return provisionPage.map(provision -> {
            ProvisionResponse provisionResponse = ProvisionMapper.convertToResponse(provision);

            List<String> images = imageRepository.findAllByNameAndReferenceId("PROVISION", provision.getProvisionId())
                    .stream()
                    .map(ImageMapper::convertToResponse)
                    .map(ImageResponse::getImageFileName)
                    .collect(Collectors.toList());

            provisionResponse.setImages(images);
            return provisionResponse;
        });
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
    public Provision updateProvision(Long provisionId, ProvisionRequest provisionRequest) {
        Provision provisionUpdate = provisionRepository.findById(provisionId).orElse(null);
        if(provisionUpdate != null){
            setProvision(provisionUpdate, provisionRequest);
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

    public void setProvision(Provision provisionUpdate, ProvisionRequest provisionInput) {
        provisionUpdate.setProvisionName(provisionInput.getProvisionName());
        provisionUpdate.setDescription(provisionInput.getDescription());
        provisionUpdate.setStatus(provisionInput.getStatus());
        provisionUpdate.setPrice(provisionInput.getPrice());
    }

    public static Specification<Provision> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("provisionName").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("price").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), likePattern)
            );
        };
    }
}
