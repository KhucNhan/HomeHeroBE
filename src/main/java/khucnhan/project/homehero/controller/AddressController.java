package khucnhan.project.homehero.controller;

import jakarta.validation.Valid;
import khucnhan.project.homehero.dto.request.AddressRequest;
import khucnhan.project.homehero.dto.response.AddressResponse;
import khucnhan.project.homehero.service.AddressService;
import khucnhan.project.homehero.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    private Long getCurrentUserId(UserDetails userDetails) {
        return userService.getByEmail(userDetails.getUsername()).getId();
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getMyAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(addressService.getByUser(userId));
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                  @Valid @RequestBody AddressRequest request) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(addressService.create(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> update(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody AddressRequest request) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(addressService.update(id, userId, request));
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<AddressResponse> setDefault(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable Long id) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(addressService.setDefault(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable Long id) {
        Long userId = getCurrentUserId(userDetails);
        addressService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}