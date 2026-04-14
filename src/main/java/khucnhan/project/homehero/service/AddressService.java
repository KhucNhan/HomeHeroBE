package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.request.AddressRequest;
import khucnhan.project.homehero.dto.response.AddressResponse;
import java.util.List;

public interface AddressService {
    AddressResponse create(Long userId, AddressRequest request);
    AddressResponse update(Long id, Long userId, AddressRequest request);
    void delete(Long id, Long userId);
    List<AddressResponse> getByUser(Long userId);
    AddressResponse setDefault(Long id, Long userId);
}