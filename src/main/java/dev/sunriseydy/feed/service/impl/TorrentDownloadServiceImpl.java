package dev.sunriseydy.feed.service.impl;

import dev.sunriseydy.feed.service.TorrentDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import static dev.sunriseydy.feed.constants.TorrentConstant.FILE_TYPE;

/**
 * @author SunriseYDY
 * @date 2024-02-23 23:13
 */
@Service
public class TorrentDownloadServiceImpl implements TorrentDownloadService {

    @Value("${sy.torrent-feed.path}")
    private String path;

    @SneakyThrows
    @Override
    public void torrentDownload(String fileName, HttpServletResponse response) {
        // 读取指定的torrent文件
        File torrentFile = new File(path + File.separator + fileName);

        // 设置文件下载相关属性
        response.setContentType(FILE_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=" + torrentFile.getName());
        response.setContentLength((int) torrentFile.length());

        // 将文件内容写入输出流
        try (FileInputStream inputStream = new FileInputStream(torrentFile)) {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(inputStream.readAllBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
}
