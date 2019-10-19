/***********************************************************
 * @Description : FastDFS的文件操作封装
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-19 10:26
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class FastDFSClient {

    @Autowired
    private FastFileStorageClient storageClient;


    /**
     * 根据文件对象获取文件后缀
     *
     * @param file 文件对象
     * @return 文件后缀
     */
    private String getExtension(MultipartFile file) {
        String fileName = file.getName();
        return FilenameUtils.getExtension(fileName);
    }

    /**
     * 上传文件，可以自己指定扩展名
     *
     * @param file          文件对象
     * @param fileExtension 文件扩展名，比如jpg、png等
     * @return 在FastDFS中的相对路径
     */
    public String uploadFile(MultipartFile file, String fileExtension) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtension, null);
        return storePath.getPath();
    }

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     */
    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, getExtension(file));
    }

    public String uploadFile2(MultipartFile file, String extension) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extension, null);
        return storePath.getPath();
    }

    /**
     * 上传文件并生成缩略图，默认是80*80的
     *
     * @param file 文件对象
     * @return 图像的相对地址
     */
    public String uploadFile2(MultipartFile file) throws IOException {
        return uploadFile2(file, getExtension(file));
    }

    /**
     * 上传二维码
     *
     * @param file 文件对象
     * @return 文件的访问相对路径
     */
    public String uploadQRCode(MultipartFile file) throws IOException {
        return uploadFile(file, "png");
    }

    /**
     * 上传头像
     *
     * @param file 文件对象
     * @return 文件的相对访问路径
     */
    public String uploadFace(MultipartFile file) throws IOException {
        return uploadFile2(file, "png");
    }

    /**
     * 上传base64图片
     *
     * @param file 文件对象
     * @return 文件的相对访问路径
     */
    public String uploadBase64(MultipartFile file) throws IOException {
        return uploadFile2(file, "png");
    }

    /**
     * 将一段字符串生成一个文件上传
     *
     * @param content       文件内容
     * @param fileExtension 文件扩展名
     * @return 文件的存储路径
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
        return storePath.getPath();
    }

    /**
     * 删除FastDFS中的文件
     *
     * @param fileUrl 文件访问地址
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            e.getMessage();
        }
    }
}