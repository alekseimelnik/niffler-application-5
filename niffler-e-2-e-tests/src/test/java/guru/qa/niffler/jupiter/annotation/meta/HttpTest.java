package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.extension.browser.BrowserExtension;
import guru.qa.niffler.jupiter.extension.http.CategoryExtensionHttp;
import guru.qa.niffler.jupiter.extension.http.SpendExtensionHttp;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        CategoryExtensionHttp.class,
        SpendExtensionHttp.class,
        BrowserExtension.class
})
public @interface HttpTest {
}
