package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.springjdbc.UserRepositorySpringJdbc;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@WebTest
public class LoginTest {

    UserRepository userRepository = new UserRepositorySpringJdbc();
    UserEntity userDataUser;

    @BeforeEach
    void createUserForTest() {

        UserAuthEntity user = new UserAuthEntity();
        user.setUsername("jdbc_user");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        userRepository.createUserInAuth(user);

        UserEntity userEntity = new UserEntity();
        userDataUser.setUsername("jdbc_user");
        userDataUser.setCurrency(CurrencyValues.RUB);

        userDataUser = userRepository.createUserInUserData(userDataUser);
    }

    @User
    @Test
    void loginTest(UserJson user) {
        WelcomePage welcomePage = new WelcomePage();
        LoginPage loginPage = new LoginPage();

        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLoginBtn();
        loginPage.doLogin(user.username(), user.testData().password());
        $(".header__avatar").shouldBe(visible);

        userRepository.findUserInUserDataById(userDataUser.getId());
    }
}
