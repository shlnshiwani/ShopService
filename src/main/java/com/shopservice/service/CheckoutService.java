package com.shopservice.service;

import com.shopservice.dto.ShippingDto;
import com.shopservice.entity.ShippingInfo;
import com.shopservice.repository.ShippingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ShippingInfoRepository shippingRepo;

    public ShippingDto getShipping(String token) {
        return shippingRepo.findBySessionToken(token)
                .map(this::toDto)
                .orElse(null);
    }

    @Transactional
    public void saveShipping(ShippingDto dto) {
        ShippingInfo info = shippingRepo.findBySessionToken(dto.getSessionToken())
                .orElse(ShippingInfo.builder().sessionToken(dto.getSessionToken()).build());
        info.setName(dto.getName());
        info.setEmail(dto.getEmail());
        info.setAddress(dto.getAddress());
        info.setCity(dto.getCity());
        info.setZip(dto.getZip());
        info.setCountry(dto.getCountry());
        info.setDeliveryDate(dto.getDeliveryDate());
        shippingRepo.save(info);
    }

    private ShippingDto toDto(ShippingInfo s) {
        ShippingDto dto = new ShippingDto();
        dto.setSessionToken(s.getSessionToken());
        dto.setName(s.getName());
        dto.setEmail(s.getEmail());
        dto.setAddress(s.getAddress());
        dto.setCity(s.getCity());
        dto.setZip(s.getZip());
        dto.setCountry(s.getCountry());
        dto.setDeliveryDate(s.getDeliveryDate());
        return dto;
    }
}
