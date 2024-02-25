package dev.sunriseydy.feed.controller;

import dev.sunriseydy.feed.service.TorrentFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SunriseYDY
 * @date 2024-02-22 17:49
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/torrent/feed")
public class TorrentFeedController {

    private final TorrentFeedService torrentFeedService;

    @GetMapping
    public ResponseEntity<?> generateRssFeed() {
        return ResponseEntity.ok(torrentFeedService.generateRssFeed());
    }
}
