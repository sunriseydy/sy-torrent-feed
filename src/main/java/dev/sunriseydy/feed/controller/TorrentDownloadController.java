package dev.sunriseydy.feed.controller;

import dev.sunriseydy.feed.service.TorrentDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SunriseYDY
 * @date 2024-02-23 22:51
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/torrent/download")
public class TorrentDownloadController {

    private final TorrentDownloadService torrentDownloadService;

    @GetMapping("/{fileName}")
    public void torrentDownload(@PathVariable String fileName, HttpServletResponse response) {
        torrentDownloadService.torrentDownload(fileName, response);
    }
}
