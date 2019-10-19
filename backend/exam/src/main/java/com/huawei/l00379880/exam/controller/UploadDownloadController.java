package com.huawei.l00379880.exam.controller;

import com.huawei.l00379880.exam.qo.DownloadQo;
import com.huawei.l00379880.exam.qo.UploadModel;
import com.huawei.l00379880.exam.qo.UploadModel2;
import com.huawei.l00379880.exam.utils.FileTransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/***********************************************************
 * @note      : 文件上传下载的接口,由于Swagger的问题导致在SwaggerUI
 *              里测试不成功，实际上前端是完全没有问题的
 * @author    : l00379880 梁山广
 *  * 为了支持Ajax请求和响应，最简单的解决方案返回一个ResponseEntity。
 *  * 以下示例演示了上传文件的三种可能方式：
 *  * 1. 单文件上传 - `MultipartFile`
 *  * 2. 多文件上传 - `MultipartFile []`
 *  * 3. 将文件上传到模型 - `@ModelAttribute`
 * @version   : V1.0 at 2018/7/16 20:43
 ***********************************************************/
@RestController
@Api(tags = "Upload And Download APIs")
@RequestMapping("/file")
@Slf4j
public class UploadDownloadController {

//    @Autowired
//    AITestConfig aiTestConfig;
//
//    @PostMapping("/upload/single")
//    @ApiOperation("单文件上传")
//    public String uploadFile(@RequestParam("file") MultipartFile uploadfile) {
//        return FileTransUtil.uploadFile(uploadfile, "/root/" + File.separator + uploadfile.getOriginalFilename());
//    }

    @ApiOperation("单文件上传,支持同时传入参数")
    @PostMapping("/api/upload/singleAndparas")
    public String uploadFileSingle(@RequestParam("dir") String dir, @RequestParam("file") MultipartFile uploadfile) {
        return FileTransUtil.uploadFile(uploadfile, dir);
    }

    @ApiOperation("单文件上传,支持同时传入参数,Model")
    @PostMapping("/upload/single/model")
    public String singleUploadFileModel(@ModelAttribute("model") UploadModel2 model) {
        return FileTransUtil.uploadFile(model.getFile(), model.getDir());
    }

    @ApiOperation("多文件上传,支持同时传入参数")
    @PostMapping("upload/multiAndparas")
    public String uploadFileMulti(@RequestParam("dir") String dir, @RequestParam("files") MultipartFile[] uploadfiles) {
        return FileTransUtil.uploadFiles(uploadfiles, dir);
    }

    @ApiOperation("多文件上传,支持同时传入参数")
    @PostMapping(value = "/upload/multi/model")
    public String multiUploadFileModel(@ModelAttribute(("model")) UploadModel model) {
        return FileTransUtil.uploadFiles(model.getFiles(), model.getDir());
    }

    @ApiOperation("Get下载文件")
    @GetMapping(value = "/download/get")
    public ResponseEntity<InputStreamResource> downloadFileGet(@RequestParam String filePath) throws IOException {
        return FileTransUtil.downloadFile(filePath);
    }

    @ApiOperation("Post下载文件")
    @PostMapping(value = "/download/post")
    public ResponseEntity<InputStreamResource> downloadFilePost(@RequestBody DownloadQo downloadQo) throws IOException {
        return FileTransUtil.downloadFile(downloadQo.getPath());
    }
}
