package com.www.springscheudler.service;

import com.www.springscheudler.model.FcmSendDeviceDto;
import com.www.springscheudler.model.FcmSendDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * FCM SERVICE
 *
 * @author : lee
 * @fileName : FcmService
 * @since : 2/21/24
 */
@Service
public interface FcmService {
    void sendMessageTo(FcmSendDto fcmSendDto) throws IOException;

    List<FcmSendDeviceDto> selectFcmSendList();

}
