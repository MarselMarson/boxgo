package com.szd.boxgo.controller;

import com.szd.boxgo.entity.RouteSegment;
import com.szd.boxgo.repo.CityRepo;
import com.szd.boxgo.service.RouteSegmentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/listing")
@RequiredArgsConstructor
public class ListingRedirectController {
    private final RouteSegmentService segmentService;
    private final CityRepo cityRepo;

    private static final String APP_STORE_URL = "https://apps.apple.com/ru/app/join-go/id6621265139";
    private static final String PLAY_STORE_URL = "market://details?id=com.szd.joingo";
    private static final String APP_LINK = "gobox://go-box.tech/listing/";
    private static final String DEFAULT_EVENT_IMAGE_URL = "https://i.postimg.cc/tTqBvMkk/luxa-org-color-changed-2024-06-01-23-27-31-2.jpg";

    @GetMapping("/{segmentId}")
    public String getOffer(Model model,
                           HttpServletRequest request,
                           @PathVariable Long segmentId
    ) {
        RouteSegment segment = segmentService.findById(segmentId);
        String cityFrom = cityRepo.findCityNameByIdAndLanguage(segment.getDepartureCity().getId(), "en");
        String cityTo = cityRepo.findCityNameByIdAndLanguage(segment.getArrivalCity().getId(), "en");

        String userAgent = request.getHeader(HttpHeaders.USER_AGENT).toLowerCase();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        model.addAttribute("listingTitle", cityFrom + " -> " + cityTo);
        model.addAttribute("siteName", "GO-BOX");
        model.addAttribute("listingDescription", segment.getDepartureAt().format(formatter));
        model.addAttribute("listingImageUrl", DEFAULT_EVENT_IMAGE_URL);
        model.addAttribute("listingUrl", "https://go-box.tech/listing/" + segmentId);

        if (userAgent.contains("telegrambot") ||
            userAgent.contains("facebookexternalhit") ||
            userAgent.contains("facebot") ||
            userAgent.contains("twitterbot") ||
            userAgent.contains("linkedinbot") ||
            userAgent.contains("whatsapp") ||
            userAgent.contains("viber") ||
            userAgent.contains("slackbot") ||
            userAgent.contains("skypeuripreview") ||
            userAgent.contains("discordbot") ||
            userAgent.contains("pinterest") ||
            userAgent.contains("redditbot") ||
            userAgent.contains("googlebot") ||
            userAgent.contains("teams") ||
            userAgent.contains("baiduspider") ||
            userAgent.contains("yandexbot") ||
            userAgent.contains("snapchat") ||
            userAgent.contains("applebot") ||
            userAgent.contains("tiktok")
        ) {
            return "socialPreview"; // Шаблон с мета-тегами
        }

        if (userAgent.contains("android")) {
            model.addAttribute("redirectUrl", APP_LINK + segmentId);
            model.addAttribute("storeUrl", PLAY_STORE_URL);
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
            model.addAttribute("redirectUrl", APP_LINK + segmentId);
            model.addAttribute("storeUrl", APP_STORE_URL);
        } else {
            model.addAttribute("redirectUrl", "https://go-box.tech/hi");
            model.addAttribute("storeUrl", "https://go-box.tech/hi");
        }

        return "openInApp";
    }
}
