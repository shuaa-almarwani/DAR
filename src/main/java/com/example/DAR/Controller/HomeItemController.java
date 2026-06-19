package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.HomeItemDTOIn;
import com.example.DAR.Enums.HomeItemCategory;
import com.example.DAR.Enums.HomeItemStatus;
import com.example.DAR.Service.HomeItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home-item")
@RequiredArgsConstructor
public class HomeItemController {

    private final HomeItemService homeItemService;

    @GetMapping("/get")
    public ResponseEntity<?> getHomeItems() {
        return ResponseEntity.status(200).body(homeItemService.getAll());
    }

    @PostMapping("/add/{homeId}")
    public ResponseEntity<?> addHomeItem(@PathVariable Integer homeId, @RequestBody @Valid HomeItemDTOIn homeItemDTOIn) {
        homeItemService.addHomeItem(homeId, homeItemDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Home item added successfully"));
    }

    @PutMapping("/update/{id}/{homeId}")
    public ResponseEntity<?> updateHomeItem(@PathVariable Integer id, @PathVariable Integer homeId, @RequestBody @Valid HomeItemDTOIn homeItemDTOIn) {
        homeItemService.updateHomeItem(id, homeId, homeItemDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Home item updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHomeItem(@PathVariable Integer id) {
        homeItemService.deleteHomeItem(id);
        return ResponseEntity.status(200).body(new ApiResponse("Home item deleted successfully"));
    }



    @GetMapping("/get/{id}")
    public ResponseEntity<?> getHomeItem(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(homeItemService.getHomeItem(id));
    }

    @GetMapping("/get/home/{homeId}")
    public ResponseEntity<?> getHomeItemsByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(homeItemService.getHomeItemsByHome(homeId));
    }

    @GetMapping("/get/home/{homeId}/category/{category}")
    public ResponseEntity<?> getHomeItemsByCategory(@PathVariable Integer homeId, @PathVariable HomeItemCategory category) {
        return ResponseEntity.status(200).body(homeItemService.getHomeItemsByCategory(homeId, category));
    }

    @GetMapping("/get/home/{homeId}/status/{status}")
    public ResponseEntity<?> getHomeItemsByStatus(@PathVariable Integer homeId, @PathVariable HomeItemStatus status) {
        return ResponseEntity.status(200).body(homeItemService.getHomeItemsByStatus(homeId, status));
    }

    @GetMapping("/get/home/{homeId}/upcoming-service")
    public ResponseEntity<?> getUpcomingServiceItems(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(homeItemService.getUpcomingServiceItems(homeId));
    }

    @GetMapping("/get/home/{homeId}/search")
    public ResponseEntity<?> searchHomeItems(@PathVariable Integer homeId, @RequestParam String keyword) {
        return ResponseEntity.status(200).body(homeItemService.searchHomeItems(homeId, keyword));
    }

    @GetMapping("/get/home/{homeId}/summary")
    public ResponseEntity<?> getHomeItemSummary(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(homeItemService.getHomeItemSummary(homeId));
    }

}
