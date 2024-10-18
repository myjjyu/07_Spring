package kr.gilju.mailer.controllers;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import kr.gilju.mailer.helpers.FileHelper;
import kr.gilju.mailer.helpers.MailHelper;
import kr.gilju.mailer.helpers.UtilHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OrderController {

  @Autowired
  private FileHelper fileHelper = null;

  @Autowired
  private MailHelper mailHelper = null;

  @Autowired
  private UtilHelper utilHelper = null;

  @PostMapping("/order_ok")
  public String orderOk(
    HttpServletResponse response,
    @RequestParam("order-name") String orderName,
    @RequestParam("order-email") String orderEmail,
    @RequestParam("order-price") int orderPrice {

      /** 1) db에서 상품정보를 가져 왔다고 가정 -> 상품명, 주문수량 */
      String productName = "내가 주문한 상품명";
      int qty = 1;

      /** 2) 결제 일자와 주문번호 생성  */
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1;
      int date = cal.get(Calendar.DATE);

      // 결제일자
      String orderDate =  String.format("%d년 %d월 %d일", year, month,date);
      
      // 주문번호  -> 년원일 + 그날 몇번째 주문인지에 대한 값
      // 데이터베이스 에서 카운트하면 주문수량 확인 가능함
      // 78은 78번째 주문인걸 말함
      String orderNumber = String.format("%04d%02d%02d%04d", year, month, date, 78);

      /** 3) 주문정보에 따른 db 처리 */
      // 주문정보를 db에 저장하는 로직은 생략!!

      /** 4) 주문정보를 메일로 발송 */
      String mailTemplatePath = "src/main/resources/mail_templates/order_result.html";
      log.debug("메일 템플릿 경로 : " + mailTemplatePath);

      // 매일 템플릿 파일 가져오기 
      String template = null;

      try {
          template = fileHelper.readString(mailTemplatePath);
      } catch (Exception ex) {
        utilHelper.redirect(response, 500, null, "메일 템플릿을 읽을 수 없습니다");
        ex.printStackTrace();
        return null;
      }


      // 메일 템플릿 안의 치환자 처리
      template = template.replace("{userName}", orderName);
      template = template.replace("{orderNumber}", orderNumber);
      template = template.replace("{productName}", productName);
      template = template.replace("{qty}", String.valueOf(qty));
      template = template.replace("{orderDate}", orderDate);
      template = template.replace("{orderPrice}", String.valueOf(orderPrice));

      String subject = orderName + "님의 주문이 완료 되었습니다";

      try {
        mailHelper.sendMail(orderEmail, subject, template);
      } catch (Exception e) {
        utilHelper.redirect(response, 500, null, "메일 발송에 실패했습니다");
        ex.printStackTrace();
        return null;
      } 

      // 주문 결과 페이지로 이동
      return "redirect:/order_result";
    }
    @GetMapping("/order_result")
    public String orderResult(){
      return "order_result";
    }
  )
  }