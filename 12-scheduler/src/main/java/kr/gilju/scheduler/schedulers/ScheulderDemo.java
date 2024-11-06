package kr.gilju.scheduler.schedulers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class ScheulderDemo {

  /**
   * 하나의 메서드가 끝나는 시간 기준, 지정된 millseconds 간격으로 실행
   * 하나의 인스턴스만 항상 실행되도록 해야 할 상황에서 유용
   * 이전 작업이 끝난 후 지정된 지연 시간(여기서는 1000밀리초 = 1초)
   * 동안 기다렸다가 다시 작업을 실행하도록 합니다.
   * 3초 동안 대기 상태에 있다가 실행이 끝
   * 즉! 하나의 작업이 끝나면 다른 하나가 동작하는 구조
   */
  @Scheduled(fixedDelay = 1000)
  public void sample1() throws InterruptedException {
    System.out.println("[sample1] 시작 + LocalDateTime.now()");
    Thread.sleep(3000);
    System.out.println("[sample1] 끝 + LocalDateTime.now()");
  }

  /**
   * 해당 메서드가 시작하는 시간 기준, 지정된millseconds 간격으로 실행
   * ==> 메서드 시작 시점 기준 으로 1초 간격마다 메서드를 실행
   * 모든 실행이 독립적인 경우에 유용
   * 메서드 수행시간이 지정된 간격보다 긴 경우 메서드가 중복실행(=병렬)될수 있다
   * ==> @Async도 사용하여 병렬로 실행될 수 있게 설정되어 있어,
   * ==> 메서드 수행 시간이 1초보다 길더라도 1초마다 새로운 메서드 실행
   * 병렬로 scheduler 를 사용할 경우, 클래스에 @EnableAsync, method에 @Async 추가
   * ==> 1초마다 새 인스턴스가 생성되어, 3초간 수행 후 로그를 출력
   * ==> 결과적으로 Async 사용해서 병렬이 가능하기때문에 실행결과가 겹쳐진다
   * 
   */
  @Async
  @Scheduled(fixedRate = 1000)
  public void sample2() throws InterruptedException {
    System.out.println("[sample2] 시작 + LocalDateTime.now()");
    Thread.sleep(3000);
    System.out.println("[sample2] 끝 + LocalDateTime.now()");
  }

  /**
   * initialDelay값 이후 처음 실행되고 fixedDelay 값에 따라 계속 실행
   * ==> 5초후 처음 시작되며 1초마다 재실행됨
   */
  @Scheduled(initialDelay = 5000, fixedDelay = 1000)
  public void sample3() throws InterruptedException {
    System.out.println("[sample3] 시작 + LocalDateTime.now()");
    Thread.sleep(3000);
    System.out.println("[sample3] 끝 + LocalDateTime.now()");
  }

  /**
   * 지정된 스케쥴에 따라 실행
   * 0 08 * * * ? ==> 초 분 시 일 월 ?
   * 
   * 1초마다 실행되는 작업 : * * * * * ?
   * 매 분 0초에 실행되는 작업 : 0 * * * * ?
   * 매 분 10초마다 실행 : 10 * * * * ? ==> 1시 0 분 10초, 1분10초, 1시 2분 10초...
   * 매 10초마다 실행 : 0/10 * * * * ? ==> 1시0분0초, 1분0분10초, 1시0분30초...
   * 매시 정각에 실행되는 작업 : 0 0 * * * ?
   * 매일 자정에 실행되는 작업 : 0 0 0 * * ?
   * 
   * http://www.cronmaker.com/
   * 
   */
  @Scheduled(cron = "0 08 * * * ?")
  public void sample4() throws InterruptedException {
    System.out.println("[sample4] 시작 + LocalDateTime.now()");
    Thread.sleep(3000);
    System.out.println("[sample4] 끝 + LocalDateTime.now()");
  }
}
