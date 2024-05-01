package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.model.UserJson.simpleUser;

public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final Map<User.Selector, Queue<UserJson>> USERS = new ConcurrentHashMap<>();

    static {
        USERS.put(User.Selector.FRIEND, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("Alex","Pass123"),
                        simpleUser("Shmel","Pass123")
                ))
        );
        USERS.put(User.Selector.INVITE_SENT, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("Anna","Pass123"),
                        simpleUser("Emilia","Pass123")
                ))
        );
        USERS.put(User.Selector.INVITE_RECEIVED, new ConcurrentLinkedQueue<>(
                List.of(
                        simpleUser("Ivan","Pass123"),
                        simpleUser("Petr","Pass123")
                ))
        );
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Method testMethod = context.getRequiredTestMethod();

        List<Method> beforeEachMethods = Arrays.stream(
                context.getRequiredTestClass().getDeclaredMethods()
        ).filter(i -> i.isAnnotationPresent(BeforeEach.class)).toList();

        List<Method> methods = new ArrayList<>();
        methods.add(testMethod);
        methods.addAll(beforeEachMethods);

        List<Parameter> parameters = methods.stream()
                .flatMap(m -> Arrays.stream(m.getParameters()))
                .filter(p -> p.isAnnotationPresent(User.class))
                .toList();

        Map<User.Selector, List<UserJson>> users = new HashMap<>();

        for (Parameter parameter : parameters) {
            User.Selector selector = parameter.getAnnotation(User.class).selector();

            if (users.containsKey(selector)) {
                continue;
            }

            UserJson userForTest = null;
            Queue<UserJson> queue = USERS.get(selector);
            while (userForTest == null) {
                userForTest = queue.poll();
            }
            users.put(selector, Collections.singletonList(userForTest));
        }

        Allure.getLifecycle().updateTestCase(testCase -> {
            testCase.setStart(new Date().getTime());
        });
        context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<User.Selector, List<UserJson>> users = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if (users != null) {
            users.forEach((selector, userList) -> {
                if (userList != null && !userList.isEmpty()) {
                    UserJson userFromTest = userList.getFirst();
                    if (userFromTest != null) {
                        USERS.get(selector).add(userFromTest);
                    }
                }
            });
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserJson.class)
                && parameterContext
                .getParameter()
                .isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        User annotation = parameterContext
                .getParameter()
                .getAnnotation(User.class);
        Map<User.Selector, List<UserJson>> users = extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);
        List<UserJson> userList = users.get(annotation.selector());
        return userList.getFirst();
    }
}
