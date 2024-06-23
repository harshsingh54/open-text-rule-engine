package com.example.debricked.rule_engine.services;


import com.example.debricked.rule_engine.commons.Constants;
import com.example.debricked.rule_engine.commons.CustomMultipartFile;
import com.example.debricked.rule_engine.model.*;
import com.example.debricked.rule_engine.repository.DependencyFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
@Slf4j
public class DebrickedService {

    @Value("${debricked.credentials.username}")
    private String userName;
    @Value("${debricked.credentials.password}")
    private String password;

    @Value("${debricked.url.auth_url}")
    private String auth_url;
    @Value("${debricked.url.uploadDependencyUrl}")
    private String uploadDependencyUrl;
    @Value("${debricked.url.queueScanUrl}")
    private String queueScanUrl;
    @Value("${debricked.url.statusUrl}")
    private String statusUrl;

    private RestClient restClient= RestClient.create();
    private FileProcessingService fileProcessingService;


    public DebrickedService(FileProcessingService fileProcessingService){
        this.fileProcessingService= fileProcessingService;
    }

    private String getAuthToken(){
        String url = auth_url;

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(userName);
        authRequest.setPassword(password);
        String token = "";

        try {
            ResponseEntity<TokenResponse> responseEntity = restClient.post()
                    .uri(url).body(authRequest).retrieve().toEntity(TokenResponse.class);

            TokenResponse tokenResponse= responseEntity.getBody();

            log.info("Token generated");

            token=tokenResponse.getToken();
        } catch (RestClientException e) {
            log.error("Exception while fetching token", e);
        }

        return token;

    }

    private String uploadDependencyFile(String token, UploadDependencyFilesRequest request) {
        String url = uploadDependencyUrl;
        String ciUploadId= "";

//        MultipartFile multipartFile = new CustomMultipartFile(request.getFileData(), request.getFileName());

        byte[] fileContent=request.getFileData();

        String fileName=request.getFileName();
        log.info("FileName :{}, path :{}", fileName, request.getFileRelativePath());
        File file = new File(fileName);

        try {
            Files.write(file.toPath(), fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileSystemResource fileSystemResource = new FileSystemResource(file);

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add(Constants.UPLOAD_DEPENDENCY_FILE_DATA, fileSystemResource); //Required
        form.add(Constants.UPLOAD_DEPENDENCY_REPO_NAME, request.getRepositoryName());//Required
        form.add(Constants.UPLOAD_DEPENDENCY_COMMIT_NAME, request.getCommitName()); //Required
        form.add(Constants.UPLOAD_DEPENDENCY_FILE_RELATIVE_PATH, request.getFileRelativePath());
        form.add(Constants.UPLOAD_DEPENDENCY_REPO_URL, request.getRepositoryUrl());
        form.add(Constants.UPLOAD_DEPENDENCY_BRANCH_NAME, request.getBranchName());
        form.add(Constants.UPLOAD_DEPENDENCY_DEFAULT_BRANCH_NAME, request.getDefaultBranchName());
        if(request.getCiUploadId()!=null && !request.getCiUploadId().isBlank()) {
            form.add(Constants.UPLOAD_DEPENDENCY_CI_UPLOAD_ID, request.getCiUploadId());
        }

        try {
            String finalToken = token;
            ResponseEntity<UploadDependencyFilesResponse> responseEntity = restClient.post()
                    .uri(url).headers(httpHeaders ->{
                        httpHeaders.setBearerAuth(finalToken);
                        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                    }).body(form).retrieve()
                    .toEntity(UploadDependencyFilesResponse.class);

            UploadDependencyFilesResponse filesResponse= responseEntity.getBody();

            ciUploadId=filesResponse.getCiUploadId();
            log.info("Upload Id : {} filename :{}", ciUploadId, request.getFileRelativePath());
        } catch (RestClientException e) {
            log.error("Exception while uploading files.", e);
        }finally {
            file.delete();
        }

        return ciUploadId;

    }

    private boolean queueFiles(String token, QueueFilesRequest request){
        String url = queueScanUrl;
        boolean isSuccess = false;

        log.info("Queue jobs for uploadid : {}", request.getCiUploadId());

        try {
            String finalToken = token;
            ResponseEntity<Void> responseEntity = restClient.post()
                    .uri(url).headers(httpHeaders ->{
                        httpHeaders.setBearerAuth(finalToken);
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    }).body(request).retrieve()
                    .toBodilessEntity();
            log.info("Queue response code :{}", responseEntity.getStatusCode());
            if(responseEntity.getStatusCode().is2xxSuccessful()){
                log.info("Status code matched");
                isSuccess=true;
            }

        } catch (RestClientException e) {
            log.error("Exception while queue request", e);
        }

        return isSuccess;

    }

    private StatusResponse getStatusByUploadId(String token, String ciUploadId){
        String url = statusUrl;

        try {
            String finalToken = token;
            ResponseEntity<StatusResponse> responseEntity = restClient.get()
                    .uri(url, ciUploadId).headers(httpHeaders ->{
                        httpHeaders.setBearerAuth(finalToken);
                        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                    }).retrieve()
                    .toEntity(StatusResponse.class);

            StatusResponse statusResponse= responseEntity.getBody();

            if(responseEntity.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))){
                log.info("Status code matched for status response");
                fileProcessingService.updateStatusByUploadId(ciUploadId, Constants.SUCCESS);
                return statusResponse;
            }

        }catch (RestClientResponseException e){
            if(e.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(429))){
                log.error("Rate limit reached for upload id :{}", ciUploadId);
            }else{
                log.error("Exception while fetching response for :{}", ciUploadId);
            }

        } catch (RestClientException e) {
            log.error("Exception while fetching status :{}", ciUploadId);
            log.error("Exception : ", e);
        }
        return null;
    }

    @Async
    public void processFiles(List<Integer> idList){

        if(idList==null || idList.isEmpty()){
            return;
        }
        List<DependencyFile> dependencyFileList = fileProcessingService.getDependencyFileByIds(idList);

        String token = getAuthToken();
        String ciUploadId = "";

        for(DependencyFile file: dependencyFileList){
            log.info("Processing Filename : {}", file.getFilename());
            UploadDependencyFilesRequest request= new UploadDependencyFilesRequest();
            request.setBranchName(file.getBranchName());
            request.setRepositoryName(file.getRepositoryName());
            request.setFileData(file.getFile());
            request.setCommitName(file.getCommitName());
            request.setFileName(file.getFilename());
            request.setDefaultBranchName(Constants.DEFAULT_BRANCH);
            request.setFileRelativePath(Constants.CONSTANT_FILE_PATH+file.getFilename());
            request.setRepositoryUrl(Constants.REPOSITORY_URL_BASE+file.getRepositoryName());
            request.setCiUploadId(ciUploadId);

            String tempCiUploadId= uploadDependencyFile(token, request);
            log.info("Temp Upload id : {}", ciUploadId);

            if(tempCiUploadId !=null && !tempCiUploadId.isBlank()){
                ciUploadId=tempCiUploadId;
                log.info("Upload id inside : {}", ciUploadId);
                fileProcessingService.updateUploadIdAndStatus(ciUploadId,Constants.IN_PROGRESS, file);
            }
        }

        log.info("Upload id outside : {}", ciUploadId);

        if(ciUploadId!=null && !ciUploadId.isBlank()){
            QueueFilesRequest queueFilesRequest=new QueueFilesRequest();
            queueFilesRequest.setCiUploadId(ciUploadId);
            queueFilesRequest.setReturnCommitData("false");
            queueFiles(token, queueFilesRequest);
        }

    }


    public StatusResponse getResponseForUploadId(String uploadId){

        if(uploadId==null || uploadId.isBlank()){
            log.info("Upload id is invalid : {}", uploadId);
            return null;
        }

        String token = getAuthToken();
        StatusResponse statusResponse = getStatusByUploadId(token, uploadId);
        log.info("Status response : {}", statusResponse);
        return statusResponse;
    }


}
