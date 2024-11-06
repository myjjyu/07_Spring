package kr.gilju.fileupload.controllers.apis;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.gilju.fileupload.helpers.FileHelper;
import kr.gilju.fileupload.helpers.RestHelper;
import kr.gilju.fileupload.models.UploadItem;

@RestController
public class UploadRestController {

  @Autowired
  private RestHelper restHelper;

  @Autowired
  private FileHelper fileHelper;

  @PostMapping("/ajax/upload_ok")
  public Map<String, Object> uploadOk(Model model,
      @RequestParam(value = "profile-photo", required = false) MultipartFile proFilePhoto) {
    UploadItem item = null;

    try {
      item = fileHelper.saveMultipartFile(proFilePhoto);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", item);

    return restHelper.sendJson(data);
  }


  @PostMapping("/ajax/upload_multi_ok")
  public Map<String, Object> uploadMultiOk(Model model,
  @RequestParam(value = "profile-photo", required = false) MultipartFile[] proFilePhoto) {

    List<UploadItem> itemList =null;

    try {
      itemList = fileHelper.saveMultipartFile(proFilePhoto);
    } catch (Exception e) {
      return restHelper.serverError(e);
    }

    Map<String, Object> data = new LinkedHashMap<String, Object>();
    data.put("item", itemList);

    return restHelper.sendJson(data);
}
}
