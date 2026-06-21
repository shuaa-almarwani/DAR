package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.HomeItemDTOIn;
import com.example.DAR.DTO.In.TroubleshootingDTOIn;
import com.example.DAR.DTO.Out.AiAdviceDTOOut;
import com.example.DAR.DTO.Out.HomeItemDTOOut;
import com.example.DAR.DTO.Out.HomeItemSummaryDTOOut;
import com.example.DAR.DTO.Out.NearbyPlaceDTOOut;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.HomeItem;
import com.example.DAR.Enums.HomeItemCategory;
import com.example.DAR.Enums.HomeItemStatus;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeItemService {

    private final HomeItemRepository homeItemRepository;
    private final HomeRepository homeRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final OverpassService overpassService;
    private final AiService aiService;
    private final ModelMapper modelMapper;

    public List<HomeItemDTOOut> getAll() {
        List<HomeItem> homeItems = homeItemRepository.findAll();

        return homeItems.stream().map(h -> modelMapper.map(h, HomeItemDTOOut.class)).toList();
    }

    public void addHomeItem(Integer homeId, HomeItemDTOIn homeItemDTOIn) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(home.getUser().getId(), UserSubscriptionStatus.ACTIVE);
        if (subscription == null) {
            throw new ApiException("Active subscription not found");
        }
        if (homeItemRepository.countHomeItemsByHomeUserId(home.getUser().getId()) >= subscription.getSubscriptionPlan().getMaxItems()) {
            throw new ApiException("You have reached the maximum number of items for your subscription");
        }
        HomeItem homeItem = new HomeItem();
        homeItem.setName(homeItemDTOIn.getName());
        homeItem.setCategory(homeItemDTOIn.getCategory());
        homeItem.setCustomCategory(homeItemDTOIn.getCustomCategory());
        homeItem.setBrand(homeItemDTOIn.getBrand());
        homeItem.setModel(homeItemDTOIn.getModel());
        homeItem.setLocation(homeItemDTOIn.getLocation());
        homeItem.setInstallDate(homeItemDTOIn.getInstallDate());
        homeItem.setPurchaseDate(homeItemDTOIn.getPurchaseDate());
        homeItem.setLifespanMonth(homeItemDTOIn.getLifespanMonth());
        homeItem.setWarrantyMonths(homeItemDTOIn.getWarrantyMonths());
        homeItem.setNextServiceDate(homeItemDTOIn.getNextServiceDate());
        homeItem.setImageUrl(homeItemDTOIn.getImageUrl());
        homeItem.setStatus(homeItemDTOIn.getStatus());
        homeItem.setNotes(homeItemDTOIn.getNotes());
        homeItem.setHome(home);
        homeItemRepository.save(homeItem);
    }

    public void updateHomeItem(Integer id, Integer homeId, HomeItemDTOIn homeItemDTOIn) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        homeItem.setName(homeItemDTOIn.getName());
        homeItem.setCategory(homeItemDTOIn.getCategory());
        homeItem.setCustomCategory(homeItemDTOIn.getCustomCategory());
        homeItem.setBrand(homeItemDTOIn.getBrand());
        homeItem.setModel(homeItemDTOIn.getModel());
        homeItem.setLocation(homeItemDTOIn.getLocation());
        homeItem.setInstallDate(homeItemDTOIn.getInstallDate());
        homeItem.setPurchaseDate(homeItemDTOIn.getPurchaseDate());
        homeItem.setLifespanMonth(homeItemDTOIn.getLifespanMonth());
        homeItem.setWarrantyMonths(homeItemDTOIn.getWarrantyMonths());
        homeItem.setNextServiceDate(homeItemDTOIn.getNextServiceDate());
        homeItem.setImageUrl(homeItemDTOIn.getImageUrl());
        homeItem.setStatus(homeItemDTOIn.getStatus());
        homeItem.setNotes(homeItemDTOIn.getNotes());
        homeItem.setHome(home);
        homeItemRepository.save(homeItem);
    }

    public void deleteHomeItem(Integer id) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        homeItemRepository.deleteById(id);
    }


    public HomeItemDTOOut getHomeItem(Integer id) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        return modelMapper.map(homeItem, HomeItemDTOOut.class);
    }

    public List<HomeItemDTOOut> getHomeItemsByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<HomeItem> homeItems = homeItemRepository.findHomeItemsByHomeId(homeId);

        return homeItems.stream().map(h -> modelMapper.map(h, HomeItemDTOOut.class)).toList();
    }

    public List<HomeItemDTOOut> getHomeItemsByCategory(Integer homeId, HomeItemCategory category) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<HomeItem> homeItems = homeItemRepository.findByCategory(homeId, category);
        return homeItems.stream().map(h -> modelMapper.map(h, HomeItemDTOOut.class)).toList();
    }

    public List<HomeItemDTOOut> getHomeItemsByStatus(Integer homeId, HomeItemStatus status) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<HomeItem> homeItems = homeItemRepository.findByStatus(homeId, status);
        return homeItems.stream().map(h -> modelMapper.map(h, HomeItemDTOOut.class)).toList();
    }

    public List<HomeItemDTOOut> getUpcomingServiceItems(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        List<HomeItem> homeItems = homeItemRepository.findUpcomingService(homeId, today, nextMonth);
        return homeItems.stream().map(h -> modelMapper.map(h, HomeItemDTOOut.class)).toList();
    }

    public List<HomeItemDTOOut> searchHomeItems(Integer homeId, String keyword) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<HomeItem> homeItems = homeItemRepository.searchByNameOrBrand(homeId, keyword);
        return homeItems.stream().map(h -> modelMapper.map(h, HomeItemDTOOut.class)).toList();
    }

    public HomeItemSummaryDTOOut getHomeItemSummary(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<HomeItem> homeItems = homeItemRepository.findHomeItemsByHomeId(homeId);

        int totalItems = homeItems.size();
        int needsMaintenance = (int) homeItems.stream()
                .filter(item -> item.getStatus() == HomeItemStatus.NEEDS_MAINTENANCE)
                .count();

        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        int upcomingServiceCount = (int) homeItems.stream()
                .filter(item -> !item.getNextServiceDate().isBefore(today) && !item.getNextServiceDate().isAfter(nextMonth))
                .count();

        int averageLifePercentage = 0;
        if (!homeItems.isEmpty()) {
            int totalLifePercentage = 0;
            for (HomeItem item : homeItems) {
                long usedMonths = ChronoUnit.MONTHS.between(item.getInstallDate(), LocalDate.now());
                int remainingPercentage = (int) Math.round(((item.getLifespanMonth() - usedMonths) * 100.0) / item.getLifespanMonth());
                totalLifePercentage += Math.max(0, Math.min(100, remainingPercentage));
            }
            averageLifePercentage = totalLifePercentage / homeItems.size();
        }

        return new HomeItemSummaryDTOOut(totalItems, needsMaintenance, upcomingServiceCount, averageLifePercentage);
    }

    public List<NearbyPlaceDTOOut> getNearbyMaintenancePlaces(Integer itemId) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(itemId);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }

        Home home = homeItem.getHome();
        return overpassService.getNearbyMaintenancePlaces(home.getLatitude(), home.getLongitude(), homeItem.getCategory());
    }

    public AiAdviceDTOOut getAiMaintenanceAdvice(Integer itemId) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(itemId);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(homeItem.getHome().getUser().getId(), UserSubscriptionStatus.ACTIVE);
        if (subscription == null) {
            throw new ApiException("Active subscription not found");
        }
        if (subscription.getSubscriptionPlan().getMaxAiReportsPerMonth() <= 0) {
            throw new ApiException("AI features are not available in your subscription plan");
        }

        String advice = aiService.generateHomeItemMaintenanceAdvice(homeItem);
        List<String> adviceList = Arrays.stream(advice.split("\\n"))
                .map(line -> line.replaceFirst("^-\\s*", "").trim())
                .filter(line -> !line.isEmpty())
                .toList();

        return new AiAdviceDTOOut(adviceList);
    }

    public AiAdviceDTOOut getAiTroubleshootingSteps(Integer itemId, TroubleshootingDTOIn troubleshootingDTOIn) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(itemId);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(homeItem.getHome().getUser().getId(), UserSubscriptionStatus.ACTIVE);
        if (subscription == null) {
            throw new ApiException("Active subscription not found");
        }
        if (subscription.getSubscriptionPlan().getMaxAiReportsPerMonth() <= 0) {
            throw new ApiException("AI features are not available in your subscription plan");
        }

        String advice = aiService.generateHomeItemTroubleshootingSteps(homeItem, troubleshootingDTOIn.getIssueDescription());
        List<String> adviceList = Arrays.stream(advice.split("\\n"))
                .map(line -> line.replaceFirst("^-\\s*", "").trim())
                .filter(line -> !line.isEmpty())
                .toList();

        return new AiAdviceDTOOut(adviceList);
    }

    public AiAdviceDTOOut getAiNearbyServiceRecommendation(Integer itemId) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(itemId);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(homeItem.getHome().getUser().getId(), UserSubscriptionStatus.ACTIVE);
        if (subscription == null) {
            throw new ApiException("Active subscription not found");
        }
        if (subscription.getSubscriptionPlan().getMaxAiReportsPerMonth() <= 0) {
            throw new ApiException("AI features are not available in your subscription plan");
        }

        Home home = homeItem.getHome();
        List<NearbyPlaceDTOOut> places = overpassService.getNearbyMaintenancePlaces(
                home.getLatitude(),
                home.getLongitude(),
                homeItem.getCategory()
        );

        String advice = aiService.generateNearbyServiceRecommendation(homeItem, places);
        List<String> adviceList = Arrays.stream(advice.split("\\n"))
                .map(line -> line.replaceFirst("^-\\s*", "").trim())
                .filter(line -> !line.isEmpty())
                .toList();

        return new AiAdviceDTOOut(adviceList);
    }
}
