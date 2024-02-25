package dev.sunriseydy.feed.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author SunriseYDY
 * @date 2024-02-23 23:09
 */
public interface TorrentDownloadService {
    /**
     * 下载种子文件
     * @param fileName 种子文件名
     * @param response
     */
    void torrentDownload(String fileName, HttpServletResponse response);
}
