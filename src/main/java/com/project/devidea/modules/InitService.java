package com.project.devidea.modules;

import com.project.devidea.infra.config.security.SHA256;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.content.study.StudyRole;
import com.project.devidea.modules.content.study.StudySampleGenerator;
import com.project.devidea.modules.content.study.StudyService;
import com.project.devidea.modules.content.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
@DependsOn(value={"TagService", "ZoneService"})
public class InitService {
    private final AccountRepository accountRepository;
    private final StudyRepository studyRepository;
    private final StudyService studyService;
    private final StudySampleGenerator studySampleGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @PostConstruct
    @Transactional
    void Setting(){
        if(accountRepository.count()!=0) return;
        Account account=new Account().builder()
                .nickname("DevIdea")
                .email("devidea@devidea.com")
                .emailCheckToken("abcdefghijklmn")
                .bio("bio")
                .gender("남성")
                .roles("ROLE_USER")
                .name("devidea")
                .password("{bcrypt}" + bCryptPasswordEncoder.encode("1234"))
                .joinedAt(LocalDateTime.now())
                .build();
        Account account2=new Account().builder()
                .nickname("테스트_회원")
                .email("test@test.com")
                .emailCheckToken("abcdefghijklmn")
                .bio("bio")
                .gender("남성")
                .roles("ROLE_USER")
                .name("테스트_회원")
                .password("{bcrypt}" + bCryptPasswordEncoder.encode("1234"))
                .joinedAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .interests(new HashSet<>())
                .mainActivityZones(new HashSet<>())
                .build();
        Account account3=new Account().builder()
                .nickname("테스트_회원2")
                .email("test2@test.com")
                .emailCheckToken("abcdefghijklmn")
                .bio("bio")
                .gender("남성")
                .roles("ROLE_USER")
                .name("테스트_회원")
                .password("{bcrypt}" + bCryptPasswordEncoder.encode("1234"))
                .joinedAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .interests(new HashSet<>())
                .mainActivityZones(new HashSet<>())
                .build();
        Account quitAccount = Account.builder()
                .nickname("탈퇴회원")
                .email("quit@quit.com")
                .emailCheckToken("abcdefghijklmn")
                .bio("bio")
                .gender("남성")
                .roles("ROLE_USER")
                .name("탈퇴회원")
                .password("{bcrypt}" + bCryptPasswordEncoder.encode(SHA256.encrypt("1234")))
                .joinedAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .interests(new HashSet<>())
                .mainActivityZones(new HashSet<>())
                .quit(true)
                .build();
        studyRepository.saveAll(studySampleGenerator.generateDumy(30));
        accountRepository.saveAll(Arrays.asList(account,account2,account3, quitAccount));
        studyRepository.findAll().stream().forEach(study -> {
            studyService.addMember(account,study, StudyRole.팀장);
            studyService.addMember(account2,study, StudyRole.회원);
        });
    }
}