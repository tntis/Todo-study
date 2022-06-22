package jocture.todo.study.factory;

import jocture.todo.study.LoginType;
import jocture.todo.study.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginserviceFactoryV3 implements LoginServiceFactory {
    private final List<LoginService> loginServices;
    //1. new 하지 않으면 -> LinkedHashMap 객체 주입
    // 2. 일반적욿눈 HashMap 사용
    //3. Map의 키가 Enum 타입일때는 HashMap을 사용하는게 더 빠름
    //private final Map<LoginType, LoginService> cachedLoginServiceMap = new EnumMap<>(LoginType.class);

    // 캐시 종류 Memcached, Redis, EhCache

    // 오류 발견 : 1. 컴파일 시 발견 -> 2. 애플리케이션 로딩 시 발견 -> 3. 런타임시 발견(최악)

    @PostConstruct
    void postConstruct() {
        // 이 객체(LoginserviceFactoryV3)가 생성되자마자 돔
        EnumSet<LoginType> loginTypes = EnumSet.allOf(LoginType.class);
        loginTypes.forEach(loginType -> {
            try {
                LoginServiceCache.put(loginType, getLoginService(loginType));  // 캐시 저장
            } catch (Exception e) {
                log.warn("Cannot find LoginService of : {}", loginType);
            }
        });
    }

    private LoginService getLoginService(LoginType loginType) {
        LoginService loginService = loginServices.stream()
                .filter(service -> service.supports(loginType))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find LoginService of : " + loginType));
        return loginService;
    }


    @Override
    public LoginService find(LoginType loginType) {
        //  log.debug(">>> cachedLoginServiceMap:{} ", cachedLoginServiceMap.getClass());
/*        return LoginServiceCache.get(loginType) // 캐시에서 조회
                .orElseGet(() -> {
                    LoginService loginService = getLoginService(loginType);
                    LoginServiceCache.put(loginType, loginService);  // 캐시 저장
                    return loginService;
                });*/
        
        return LoginServiceCache.get(loginType)
                .orElseThrow(() -> new NoSuchElementException("Cannot find LoginService of : " + loginType));


        /* return LoginServiceCache.get(loginType);*/

    }

    private static class LoginServiceCache {
        private static final Map<LoginType, LoginService> loginServiceMap = new EnumMap<>(LoginType.class);

        public static Optional<LoginService> get(LoginType loginType) {
            return Optional.ofNullable(loginServiceMap.get(loginType));
        }

/*        // 에러를 바로 발생시키려면
        public static LoginService get(LoginType loginType) {
            return loginServiceMap.get(loginType);
        }*/

        public static void put(LoginType loginType, LoginService loginService) {
            loginServiceMap.put(loginType, loginService);
        }
    }


}
