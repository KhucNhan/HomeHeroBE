package khucnhan.project.homehero.service.impl;

import khucnhan.project.homehero.dto.request.AddressRequest;
import khucnhan.project.homehero.dto.response.AddressResponse;
import khucnhan.project.homehero.exception.BadRequestException;
import khucnhan.project.homehero.exception.ResourceNotFoundException;
import khucnhan.project.homehero.model.Address;
import khucnhan.project.homehero.model.User;
import khucnhan.project.homehero.repository.AddressRepository;
import khucnhan.project.homehero.repository.UserRepository;
import khucnhan.project.homehero.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AddressResponse create(Long userId, AddressRequest request) {
        User user = findUser(userId);
        if (Boolean.TRUE.equals(request.getIsDefault())) clearDefault(userId);

        Address address = new Address();
        address.setUser(user);
        address.setAddressLine(request.getAddressLine());
        address.setLat(request.getLat());
        address.setLng(request.getLng());
        address.setLabel(request.getLabel());
        address.setIsDefault(request.getIsDefault());

        return toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public AddressResponse update(Long id, Long userId, AddressRequest request) {
        Address address = findByIdAndUser(id, userId);
        if (Boolean.TRUE.equals(request.getIsDefault())) clearDefault(userId);

        address.setAddressLine(request.getAddressLine());
        address.setLat(request.getLat());
        address.setLng(request.getLng());
        address.setLabel(request.getLabel());
        address.setIsDefault(request.getIsDefault());

        return toResponse(addressRepository.save(address));
    }

    @Override
    public void delete(Long id, Long userId) {
        addressRepository.delete(findByIdAndUser(id, userId));
    }

    @Override
    public List<AddressResponse> getByUser(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressResponse setDefault(Long id, Long userId) {
        clearDefault(userId);
        Address address = findByIdAndUser(id, userId);
        address.setIsDefault(true);
        return toResponse(addressRepository.save(address));
    }

    private void clearDefault(Long userId) {
        addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(a -> {
            a.setIsDefault(false);
            addressRepository.save(a);
        });
    }

    private Address findByIdAndUser(Long id, Long userId) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + id));
        if (!address.getUser().getId().equals(userId))
            throw new BadRequestException("Address does not belong to user");
        return address;
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    public AddressResponse toResponse(Address a) {
        return AddressResponse.builder()
                .id(a.getId())
                .addressLine(a.getAddressLine())
                .lat(a.getLat())
                .lng(a.getLng())
                .label(a.getLabel())
                .isDefault(a.getIsDefault())
                .userId(a.getUser().getId())
                .build();
    }
}
