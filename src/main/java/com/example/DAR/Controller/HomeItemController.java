package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.HomeItemDTOIn;
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

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getHomeItem(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(homeItemService.getHomeItem(id));
    }

    @GetMapping("/get/home/{homeId}")
    public ResponseEntity<?> getHomeItemsByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(homeItemService.getHomeItemsByHome(homeId));
    }

    @PostMapping("/add/{home_id}")
    public ResponseEntity<?> addHomeItem(@PathVariable Integer home_id, @RequestBody @Valid HomeItemDTOIn homeItemDTOIn) {
        homeItemService.addHomeItem(home_id, homeItemDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Home item added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateHomeItem(@PathVariable Integer id, @RequestBody @Valid HomeItemDTOIn homeItemDTOIn) {
        homeItemService.updateHomeItem(id, homeItemDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Home item updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHomeItem(@PathVariable Integer id) {
        homeItemService.deleteHomeItem(id);
        return ResponseEntity.status(200).body(new ApiResponse("Home item deleted successfully"));
    }
}
