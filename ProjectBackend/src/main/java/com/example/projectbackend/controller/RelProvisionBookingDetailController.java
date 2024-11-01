package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.RelProvisionBookingDetailRequest;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.RelProvisionBookingDetail;
import com.example.projectbackend.service.RelProvisionBookingDetailService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/relProBook")

public class RelProvisionBookingDetailController {
    private final RelProvisionBookingDetailService relProvisionBookingDetailService;

    @GetMapping
    public List<RelProvisionBookingDetailResponse> getAllRel(){
        return relProvisionBookingDetailService.getAllRel();
    }

    @GetMapping("/{relId}")
    public RelProvisionBookingDetailResponse getDetailRel(@PathVariable Long relId){
        return relProvisionBookingDetailService.getDetailRel(relId);
    }

    @PostMapping
    public RelProvisionBookingDetail createRel(@Valid @RequestBody RelProvisionBookingDetailRequest relProvisionBookingDetailRequest){
        return  relProvisionBookingDetailService.createRel(relProvisionBookingDetailRequest);
    }

    @PutMapping("/{relId}")
    public RelProvisionBookingDetail updateRel(@PathVariable Long relId, RelProvisionBookingDetail relProvisionBookingDetail){
        return relProvisionBookingDetailService.updateRel(relId, relProvisionBookingDetail);
    }

    @DeleteMapping("/{relId}")
    public void deleteRel(@PathVariable Long relId){
        relProvisionBookingDetailService.deleteRel(relId);
    }


}
