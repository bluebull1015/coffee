package com.coffee.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateData {
    // 윈도우 폴더 구분자는 \인데 특수 문자이므로 반드시 \\로 표기해야 합니다.
    private static final String IMAGE_DIR = "c:\\boot\\images"; // 이미지가 있는 폴더 경로

    public static List<String> getImageFileNames() {
        // 특정 폴더 내에 들어 있는 모든 이미지 파일을 문자열 List 형식으로 반환합니다.
        File folder = new File(IMAGE_DIR) ;
        List<String> imageFiles = new ArrayList<String>();

        if(folder.exists() == false && folder.isFile() ){
            System.out.println("해당 폴더가 존재하지 않습니다. " + IMAGE_DIR );
            return imageFiles ;
        }

        // 확장자가 다음 항목인 파일들만 추출
        String[] imageExtensions = {".jpg", ".jpeg", ".png"} ;

        File[] fileList = folder.listFiles() ;

        for(File  file : fileList){
            // 자바의 stream API 공부해야 합니다.
            if (file.isFile() && Arrays.stream(imageExtensions)
                    .anyMatch(ext -> file.getName().toLowerCase().endsWith(ext))) {
                imageFiles.add(file.getName());
            }
        }

        return imageFiles ;
    }
}
