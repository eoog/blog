package com.www.springlogbatch.batch;



import com.www.springlogbatch.entity.WebLog;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.datafaker.Faker;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;


public class WebLogItemReader implements ItemReader<WebLog> {

  /**
   * 가짜 데이터
   */
  public static Faker faker = new Faker();
  private int count = 0;
  private static final int MAX_COUNT = 100;

  // 자주 접속하는 특정 IP
  private static final String ADDRESS_IP = "192.111.1.1";
  private static final double ADDRESS_IP_PROBABILITY = 0.7; // 특정주소 생성될 확률 (70퍼)


  @Override
  public WebLog read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

    if (count < MAX_COUNT) {
      count++;
      return genNewWebLog();
    } else {
    return null; // 더이상 데이터가 업승ㄹ때
    }
  }

  public WebLog genNewWebLog() {
    WebLog webLog = new WebLog();

    // 특정 IP 를 70 퍼 확률로 생성하고 , 나머지 랜덤
    if (Math.random() < WebLogItemReader.ADDRESS_IP_PROBABILITY) {
      webLog.setIpAddress(WebLogItemReader.ADDRESS_IP); // 특정 ip
    } else {
      webLog.setIpAddress(WebLogItemReader.faker.internet().ipV4Address()); // 랜덤
    }

    webLog.setUrl(WebLogItemReader.faker.internet().url());
    webLog.setTimestamp(WebLogItemReader.faker.date().past(7,TimeUnit.DAYS));
    webLog.setSessionId(UUID.randomUUID().toString());

    return webLog;

  }
}

