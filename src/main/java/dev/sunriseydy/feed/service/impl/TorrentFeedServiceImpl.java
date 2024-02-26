package dev.sunriseydy.feed.service.impl;

import be.christophedetroyer.torrent.Torrent;
import be.christophedetroyer.torrent.TorrentParser;
import com.rometools.rome.feed.rss.*;
import dev.sunriseydy.feed.service.TorrentFeedService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.sunriseydy.feed.constants.TorrentConstant.*;

/**
 * @author SunriseYDY
 * @date 2024-02-22 15:47
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TorrentFeedServiceImpl implements TorrentFeedService {

    private final HttpServletRequest request;

    @Value("${sy.torrent-feed.path}")
    private String path;

    @Override
    public Channel generateRssFeed() {
        log.debug("开始生成 RSS Feed");
        log.debug("路径为:{}", path);
        // 获取种子文件列表
        List<Torrent> torrents = this.getTorrents(path);
        // 生成 RSS Feed
        Channel channel = this.getChannel(torrents);
        log.debug("生成 RSS Feed 完成");
        return channel;
    }

    /**
     * 获取指定路径下的种子文件列表
     * @param path 种子文件路径
     * @return 种子文件列表
     */
    private List<Torrent> getTorrents(String path) {
        List<Torrent> result = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            result = walk
                    // 仅保留文件（忽略目录）
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    // 只保留 .torrent 文件
                    .filter(f -> f.endsWith(FILE_EXTENSION))
                    .map(f -> {
                        try {
                            Torrent torrent = TorrentParser.parseTorrent(f);
                            if (torrent != null) {
                                // 重命名种子文件
                                this.renameTorrentFile(torrent, f);
                            }
                            return torrent;
                        } catch (IOException e) {
                            log.error("解析种子文件失败：", e);
                        }
                        return null;
                    })
                    // 过滤掉空的种子文件
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("获取种子文件失败", e);
        }
        return result;
    }

    /**
     * 重命名种子文件
     * @param torrent 种子文件
     * @param path 种子文件路径
     * @throws IOException IO 异常
     */
    private void renameTorrentFile(Torrent torrent, String path) throws IOException {
        File file = new File(path);
        if (file.canWrite() && !(torrent.getInfo_hash() + FILE_EXTENSION).equals(file.getName())) {
            // 如果种子文件名不是 info_hash + .torrent，则重命名
            boolean flag = file.renameTo(new File(file.getParent() + File.separator + torrent.getInfo_hash() + FILE_EXTENSION));
            if (!flag) {
                throw new IOException("重命名种子文件失败");
            }
        }
    }

    private Channel getChannel(List<Torrent> torrents) {
        Channel channel = new Channel("rss_2.0");
        channel.setTitle("SY Torrent Rss Feed");
        channel.setDescription("SY Torrent Rss Feed");
        channel.setLink(request.getRequestURL().toString());
        channel.setGenerator("SY Torrent Rss Feed Generator - v1.0");
        channel.setEncoding("UTF-8");
        channel.setPubDate(new Date());
        List<Item> items = new ArrayList<>();
        for (Torrent torrent : torrents) {
            try {
                Item item = getItem(torrent);
                items.add(item);
            } catch (Exception e) {
                log.error("Error when generating RSS item for torrent: {}", torrent, e);
            }
        }
        channel.setItems(items);
        return channel;
    }

    private Item getItem(Torrent torrent) {
        Item item = new Item();
        item.setTitle(torrent.getName());
        item.setPubDate(torrent.getCreationDate());
        // 条目的链接
        item.setLink(request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/api/torrent/download/" + torrent.getInfo_hash() + FILE_EXTENSION);
        Guid guid = new Guid();
        guid.setValue(item.getLink());
        item.setGuid(guid);
        Enclosure torrentClosure = new Enclosure();
        torrentClosure.setType(FILE_TYPE);
        // torrent 文件的下载链接
        torrentClosure.setUrl(item.getLink());
        if (torrent.getTotalSize() != null) {
            torrentClosure.setLength(torrent.getTotalSize());
        }
        item.setEnclosures(List.of(torrentClosure));
        Description description = new Description();
        item.setDescription(description);
        return item;
    }
}
