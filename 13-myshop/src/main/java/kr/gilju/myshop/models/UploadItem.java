package kr.gilju.myshop.models;

import lombok.Data;

/**
 * 업로드된 파일의 정보를 저장하기 위한 javabeans
 * - 이 클래스의 객체가 업로드 된 파일의 수 만큼 생성되어 list 형태로 보관된다
 */
@Data
public class UploadItem {
  private String filedName; // <input type = "file"> 의 name속성
  private String originName; // 원본 파일 이름
  private String contentType; // 파일의 형식
  private long fileSize; // 파일의 용량
  private String filePath; // 서버상의 파일 경로
  private String fileUrl; // 서버상의 파일 url
  private String thumbnailPath; // 썸네일 이미지 경로
  private String thumbnailUrl; // 썸네일 이미지 url
}
