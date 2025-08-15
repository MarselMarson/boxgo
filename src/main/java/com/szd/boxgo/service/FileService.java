package com.szd.boxgo.service;

import com.szd.boxgo.dto.FileUrlDto;
import com.szd.boxgo.entity.File;
import com.szd.boxgo.repo.FileRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final FileRepo fileRepo;
    private final S3Client s3Client;

    private void saveFile(String fileName, String url) {
        fileRepo.save(
                File.builder()
                        .name(fileName)
                        .url(url)
                        .build()
        );
    }

    private static String generateUniqueFileName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String getNameFromUrl(String url) {
        // Ищем индекс ".com/" и берем всё, что идет после него
        int index = url.indexOf(".com/");
        if (index != -1) {
            return url.substring(index + 5); // 5 - это длина ".com/"
        }
        return null; // Если ".com/" не найден

        /*String regex = "/([^/?]*)\\?";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        String name = "";

        if (matcher.find()) {
            name = matcher.group(1);
        }
        return name;*/
    }

    public File getByFileName(String name) {
        return fileRepo.findByName(name).orElseThrow(() -> new EntityNotFoundException("файл с именем " + name + " не найден"));
    }

    public FileUrlDto uploadSingleFileAws(MultipartFile file) throws IOException {
        return new FileUrlDto(uploadFileAws(file));
    }

    public String uploadFileAws(MultipartFile file) throws IOException {
        String fileName = generateUniqueFileName();
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .contentType("image/jpeg")
                        .key(fileName)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );
        String url = String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
        saveFile(fileName, url);
        return url;
    }

    public List<String> uploadFilesAws(MultipartFile[] files) throws InterruptedException, ExecutionException {
        List<Callable<String>> uploadTasks = getCallables(files);

        List<Future<String>> futures = threadPoolTaskExecutor.getThreadPoolExecutor().invokeAll(uploadTasks);
        List<String> uploadedUrls = new ArrayList<>();

        for (Future<String> future : futures) {
            uploadedUrls.add(future.get());
        }

        return uploadedUrls;
    }

    @NotNull
    private List<Callable<String>> getCallables(MultipartFile[] files) {
        List<Callable<String>> uploadTasks = new ArrayList<>();

        for (MultipartFile file : files) {
            uploadTasks.add(() -> uploadFileAws(file));
        }
        return uploadTasks;
    }
}
