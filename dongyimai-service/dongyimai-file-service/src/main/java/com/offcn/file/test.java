package com.offcn.file;

import org.csource.fastdfs.*;

public class test {
    public static void main(String[] args) throws Exception {
        //   C:\Users\Administrator\Desktop\1.jpg

        String file = "C:\\Users\\29909\\Pictures\\panda.jpg";


        ClientGlobal.init("fdfs_client.conf");

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();


        StorageServer storageServer = null;

        StorageClient storageClient = new StorageClient(trackerServer, storageServer);


        String[] strings = storageClient.upload_file(file, "jpg", null);


        for (String string : strings) {
            System.out.println(string);
        }
        //  22122  在服务器上开放该端口（关闭防火墙）
        // M00 在哪出现过？ nginx
        //   http://192.168.188.129:8080/group1/M00/00/00/wKi8gWGE5gOAIXfNAACQCvs_nbs447.jpg


    }
}
