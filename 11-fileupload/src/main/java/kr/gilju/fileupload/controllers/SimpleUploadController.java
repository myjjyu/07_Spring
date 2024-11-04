package kr.gilju.fileupload.controllers;

import java.io.File;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import kr.gilju.fileupload.helpers.WebHelper;
import kr.gilju.fileupload.models.UploadItem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SimpleUploadController {

  /**
   * WebHelper주입
   */
  @Autowired
  private WebHelper webHelper;

  /**
   * 업로드된 파일이 저장될 경로 applecation.properties로 부터 읽어옴)
   */
  @Value("${upload.dir}")
  private String uploadDir;

  /**
   * 업로드된 파일이 노출될 url 경로 applecation.properties로 부터 읽어옴)
   */
  @Value("${upload.url}")
  private String uploadUrl;

  /** 업로드 폼을 구성하는 페이지 */
  @GetMapping("/simple/upload_single")
  public String uploString() {
    return "simple/upload_single";
  }

  /**
   * spring 순정 업로드 기능구현
   * 업로드가 되는 과정에서 다뤄야 하는 값들을 확인하기 위한 예제
   */
  @PostMapping("/simple/upload_single_ok")
  public String uploadOk(Model model,
      @RequestParam(value = "subject", required = false) String subject,
      // <input type="file">의 name값 photo인 항목을 받는다
      @RequestParam(value = "photo", required = false) MultipartFile photo) {

    /** 1 업로드된 파일 저장하기 */
    // 업로드된 파일이 존재하는지 확인한다
    if (photo.getOriginalFilename().isEmpty()) {
      webHelper.badRequest("업로드된 파일이 없습니다");
      return null;
    }

    // 업로드 된 파일의 정보를 로그에 기록
    log.debug("==========================");
    log.debug("원본 파일 이름 : " + photo.getOriginalFilename());
    log.debug("<input type='file'>의 name 속성값: " + photo.getName());
    log.debug("파일형식: " + photo.getContentType());
    log.debug("파일크기: " + photo.getSize());

    // 업로드된 파일이 저장될 폴더의 이름을 "년/월/일" 형식으로 생성한다
    Calendar c = Calendar.getInstance();
    String targetDir = String.format("%s/%04d/%02d/%02d", uploadDir,c.get(Calendar.YEAR),
    c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));

    //폴더가 존재하지 않는다면 생성한다
    File f = new File(targetDir);
    if (!f.exists()) {
      f.mkdirs();
    }

    //저장될 파일의 이름을 생성한다
    //파일의 원본 이름 추출
    String originName = photo.getOriginalFilename();
    //파일의 원본 이름에서 확장자만 추출
    String ext = originName.substring(originName.lastIndexOf("."));
    String fileName = null; // 웹 서버에 저장될 파일 이름
    File targetFile = null; // 저장된 파일 정보를 담기 위한 file객체
    int count = 0; // 중복된 파일 수

    // 일단 무한 루프
    while (true) { 
        // 저장될 파일이름 --> 현재시각 + 카운트값 + 확장자
        fileName = String.format("%d%d%s", System.currentTimeMillis(), count, ext);
        // 업로드 파일이 저장될 폴더 + 파일이름으로 파일객체를 생성한다
        targetFile = new File(targetDir, fileName);

        // 동일한 이름의 파일이 없다면 반복 중단.
        if(!targetFile.exists()){
          break;
        }

        //if 문을 빠져나올 경우 중복된 이름의 파일이 존재한다는 의미이르모 count를 1증가
        count++;
    }






    // 업로드 된 파일이 저장될 경로 정보를 생성한다
    // File targetFile = new File(uploadDir, photo.getOriginalFilename());

    // 업로드 된 파일을 지정된 경로로 복사한다
    // 업로드 실패를 위해 try/chtch 구문을 사용한다(직접추가)
    try {
      photo.transferTo(targetFile);
    } catch (Exception e) {
      webHelper.serverError(e);
      return null;
    }

    /**
     * 2) 업로드된 경로 정보 처리하기
     * 복사된 파일의 절대경로를 추출한다
     */
    String absPath = targetFile.getAbsolutePath().replace("\\", "/");
    log.debug("업로드된 파일의 경로: " + absPath);

    // 업로드 된 파일의 절대경로(absPath)에서 환경설정 파일에 명시된 폴더까지의 위치는 삭제하여
    // 환경설정 파일에 명시된 upload.dir 이후의 위치만 추출한다 (윈도우만 해당!!)
    String filePath = null;
    if (absPath.substring(0, 1).equals("/")) {

      // mac, linux용 경로처리
      filePath = absPath.replace(uploadDir, "");
    } else {
      // window용 경로처리 --> 설정파일에 명시한 첫 글자(/)를 제거해야 함
      filePath = absPath.replace(uploadDir.substring(1), "");
    }

    log.debug("업로드 폴더 내에서 최종 경로: " + filePath);

    /** 3) 업로드 결과를 beans에 저장 */
    UploadItem item = new UploadItem();
    item.setContentType(photo.getContentType());
    item.setFiledName(photo.getName());
    item.setFileSize(photo.getSize());
    item.setOriginName(photo.getOriginalFilename());
    item.setFilePath(filePath);

    // 업로드된 경로를 웹 상에서 접근 가능한 문자열로 변환하여 beans에 추가
    String fileUrl = String.format("%s%s", uploadUrl, filePath);
    item.setFileUrl(fileUrl);
    log.debug("파일의 url : " + fileUrl);
    log.debug("==========================");

    /** 4) view 처리 */
    // 텍스트 정보를 view로 전달한다
    model.addAttribute("subject", subject);
    // 업로드 정보를 view로 전달한다
    model.addAttribute("item", item);

    // view 호출
    return null;

  }

}
