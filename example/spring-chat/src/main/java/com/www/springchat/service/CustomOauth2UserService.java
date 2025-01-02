package com.www.springchat.service;

import com.www.springchat.entity.Member;
import com.www.springchat.enums.Gender;
import com.www.springchat.repositors.MemberRepository;
import com.www.springchat.vo.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =  super.loadUser(userRequest);

        Map<String,Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        String email =(String) kakaoAccount.get("email");
        Member member = memberRepository.findByEmail(email).orElseGet(() -> registerMember(kakaoAccount));

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }

    private Member registerMember(Map<String, Object> kakaoAccount) {
        Member member = Member.builder()
                .email(kakaoAccount.get("email").toString())
                .nickName((String) ((Map) kakaoAccount.get("profile")).get("nickname"))
                .name(kakaoAccount.get("name").toString())
                .phoneNumber(kakaoAccount.get("phone_number").toString())
                .gender(Gender.valueOf(kakaoAccount.get("gender").toString().toUpperCase()))
                .birthday(getBirthDay(kakaoAccount))
                .role("USER_ROLE")
                .build();

        return memberRepository.save(member);
    }

    private LocalDate getBirthDay(Map<String, Object> kakaoAccount) {
        String birthday = (String) kakaoAccount.get("birthday");
        String birthyear = (String) kakaoAccount.get("birthyear");

        return LocalDate.parse(birthyear + birthday, DateTimeFormatter.BASIC_ISO_DATE);
    }
}
