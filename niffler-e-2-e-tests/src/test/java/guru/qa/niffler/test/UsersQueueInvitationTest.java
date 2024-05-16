package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.Selector.*;


@WebTest
public class UsersQueueInvitationTest {

    PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void setup() {

        WelcomePage welcomePage = new WelcomePage();
        LoginPage loginPage = new LoginPage();


        Selenide.open("http://127.0.0.1:3000/");
        welcomePage.clickLoginBtn();
        loginPage.doLogin("Aleksei", "Pass123");
    }

    @Test
    void friendsTestWithFriendAndInviteSent(@User(selector = FRIEND) UserJson userForTest,
                                @User(selector = INVITE_SENT) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");

        peoplePage.isFriend(userForTest.username());
        peoplePage.isInviteSent(userForAnotherTest.username());
    }

    @Test
    void friendsTestWithFriendAndInviteReceived(@User(selector = FRIEND) UserJson userForTest,
                                            @User(selector = INVITE_RECEIVED) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");

        peoplePage.isFriend(userForTest.username());
        peoplePage.isInvitationReceived(userForAnotherTest.username());
    }

    @Test
    void friendsTestWithInviteSentAndInviteReceived(@User(selector = INVITE_SENT) UserJson userForTest,
                                                @User(selector = INVITE_RECEIVED) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");

        peoplePage.isInviteSent(userForTest.username());
        peoplePage.isInvitationReceived(userForAnotherTest.username());
    }

    @Test
    void friendsTestWithFriendAndInviteSent2(@User(selector = FRIEND) UserJson userForTest,
                                            @User(selector = INVITE_SENT) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");

        peoplePage.isFriend(userForTest.username());
        peoplePage.isInviteSent(userForAnotherTest.username());
    }

    @Test
    void friendsTestWithFriendAndInviteReceived2(@User(selector = FRIEND) UserJson userForTest,
                                                @User(selector = INVITE_RECEIVED) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");

        peoplePage.isFriend(userForTest.username());
        peoplePage.isInvitationReceived(userForAnotherTest.username());
    }

    @Test
    void friendsTestWithInviteSentAndInviteReceived2(@User(selector = INVITE_SENT) UserJson userForTest,
                                                    @User(selector = INVITE_RECEIVED) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");

        peoplePage.isInviteSent(userForTest.username());
        peoplePage.isInvitationReceived(userForAnotherTest.username());
    }

    @Test
    void friendsTestWithFriendAndFriend(@User(selector = FRIEND) UserJson userForTest,
                                        @User(selector = FRIEND) UserJson userForAnotherTest) {
        Selenide.open("http://127.0.0.1:3000/people");

        peoplePage.isFriend(userForTest.username());
        peoplePage.isFriend(userForAnotherTest.username());
    }
}
