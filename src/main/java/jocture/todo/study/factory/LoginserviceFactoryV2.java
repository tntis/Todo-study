package jocture.todo.study.factory;

import jocture.todo.study.LoginType;
import jocture.todo.study.service.LoginService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

//@Component
@RequiredArgsConstructor
public class LoginserviceFactoryV2 implements LoginServiceFactory {
    private final List<LoginService> loginServices;

    @Override
    public LoginService find(LoginType loginType) {
/*        for (LoginService loginService : loginServices) {
            // Web -> Mobile -> Google : supports()
            if (loginService.supports(loginType)) {
                return loginService;
            }
        }
        return new NoSuchElementException("Cannot find LoginService of : " + loginType);

        */

        // map(), filter(), reduce()
        return loginServices.stream()
                .filter(loginService -> loginService.supports(loginType))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find LoginService of : " + loginType));

    }
}
