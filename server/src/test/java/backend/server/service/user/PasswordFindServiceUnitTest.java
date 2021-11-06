package backend.server.service.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import static org.assertj.core.api.Assertions.*;

public class PasswordFindServiceUnitTest {

    @Test
    @DisplayName("임시 비밀번호가 무작위로 생성되는지 테스트")
    void TempPassword() {
        PasswordFindService passwordFindService = new PasswordFindService(null, null, null);

        String password1 = passwordFindService.makeTempPassword();
        String password2 = passwordFindService.makeTempPassword();

        assertThat(password1).isNotEqualTo(password2);
        assertThat(password1.length()).isEqualTo(password2.length()).isEqualTo(7);
    }
}
