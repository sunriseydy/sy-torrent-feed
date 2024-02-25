package dev.sunriseydy.feed.service;

import com.rometools.rome.feed.rss.Channel;

/**
 * @author SunriseYDY
 * @date 2024-02-22 15:47
 */
public interface TorrentFeedService {
    /**
     * 扫描指定目录下的 torrent 文件，生成 RSS Feed
     * @return
     */
    Channel generateRssFeed();
}
